package com.thacbao.neki.services.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thacbao.neki.dto.request.product.OrderFilterRequest;
import com.thacbao.neki.dto.request.product.OrderItemRequest;
import com.thacbao.neki.dto.request.product.OrderRequest;
import com.thacbao.neki.dto.response.OrderResponse;
import com.thacbao.neki.dto.response.OrderSummaryResponse;
import com.thacbao.neki.dto.response.PaymentResponse;
import com.thacbao.neki.enums.DiscountType;
import com.thacbao.neki.enums.OrderStatus;
import com.thacbao.neki.exceptions.common.InvalidException;
import com.thacbao.neki.exceptions.common.NotFoundException;
import com.thacbao.neki.model.*;
import com.thacbao.neki.repositories.jpa.*;
import com.thacbao.neki.security.SecurityUtils;
import com.thacbao.neki.security.UserPrincipal;
import com.thacbao.neki.services.*;
import com.thacbao.neki.utils.MessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductVariantRepository productVariantRepository;
    private final DiscountRepository discountRepository;

    private final ProductService productService;
    private final PaymentService paymentService;
    private final ShippingService shippingService;
    private final DiscountCalculationService discountCalculationService;
    private final PaymentLinkService paymentLinkService;
    private final DiscountService discountService;

    private static final int MAX_QUANTITY_PER_ITEM = 10;

    @Value("${PAYMENT.cod}")
    private String cod;

    @Value("${PAYMENT.payos}")
    private String payos;

    // USER OPERATIONS
    // tru kho khi doi status thu cong sang confirm cod(ok), discount fix, xoa hang
    // khi tt selected(ok), them province(ok)

    @Override
    public OrderResponse createOrderFromCart(OrderRequest request) {
        log.info("create order from cart");
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException(MessageKey.CART_EMPTY));

        if (cart.getCartItems().isEmpty()) {
            throw new InvalidException(MessageKey.CART_EMPTY);
        }

        List<OrderItemRequest> items = cart.getCartItems().stream()
                .map(cartItem -> OrderItemRequest.builder()
                        .variantId(cartItem.getVariant().getId())
                        .quantity(cartItem.getQuantity())
                        .build())
                .collect(Collectors.toList());

        Order order = createOrder(user, request, items);
        String transactionId = "ORD-" + order.getOrderNumber();
        PaymentResponse paymentResponse = paymentService.create(order, request.getPaymentMethodId(), transactionId);

        cart.getCartItems().clear();
        cartRepository.save(cart);
        if (paymentResponse.getPaymentMethod().getName().equalsIgnoreCase(payos)) {
            ObjectNode response = paymentLinkService.createPaymentLink(order);
            return OrderResponse.fromWithPaymentLink(order, response);
        }
        log.info("create order from cart success");
        return OrderResponse.from(order);
    }

    @Override
    public OrderResponse createOrderFromSelectedItems(OrderRequest request, List<OrderItemRequest> items) {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException(MessageKey.CART_EMPTY));

        if (cart.getCartItems().isEmpty()) {
            throw new InvalidException(MessageKey.CART_EMPTY);
        }
        validateOrder(items);

        Order order = createOrder(user, request, items);

        Set<Integer> orderedVariantIds = order.getOrderItems().stream()
                .map(item -> item.getVariant().getId())
                .collect(Collectors.toSet());
        cart.getCartItems().removeIf(cartItem -> orderedVariantIds.contains(cartItem.getVariant().getId()));
        cartRepository.save(cart);

        String transactionId = "ORD-" + order.getOrderNumber();
        PaymentResponse paymentResponse = paymentService.create(order, request.getPaymentMethodId(), transactionId);

        if (paymentResponse.getPaymentMethod().getName().equalsIgnoreCase(payos)) {
            ObjectNode response = paymentLinkService.createPaymentLink(order);
            return OrderResponse.fromWithPaymentLink(order, response);
        }

        return OrderResponse.from(order);
    }

    @Override
    public OrderResponse buyNow(OrderRequest request, OrderItemRequest item) {
        User user = getCurrentUser();
        validateOrder(List.of(item));
        Order order = createOrder(user, request, List.of(item));
        String transactionId = "ORD-" + order.getOrderNumber();
        PaymentResponse paymentResponse = paymentService.create(order, request.getPaymentMethodId(), transactionId);

        if (paymentResponse.getPaymentMethod().getName().equalsIgnoreCase(payos)) {
            ObjectNode response = paymentLinkService.createPaymentLink(order);
            return OrderResponse.fromWithPaymentLink(order, response);
        }

        return OrderResponse.from(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Integer orderId) {
        User user = getCurrentUser();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(MessageKey.ORDER_NOT_FOUND));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new InvalidException(MessageKey.ORDER_ACCESS_DENIED);
        }

        return OrderResponse.fromWithoutUser(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        User user = getCurrentUser();
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new NotFoundException(MessageKey.ORDER_NOT_FOUND));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new InvalidException(MessageKey.ORDER_ACCESS_DENIED);
        }

        return OrderResponse.fromWithoutUser(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderSummaryResponse> getMyOrders(Pageable pageable) {
        User user = getCurrentUser();
        Page<Order> orders = orderRepository.findByUserIdWithPagination(user.getId(), pageable);
        return orders.map(OrderSummaryResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderSummaryResponse> getMyOrdersByStatus(String status, Pageable pageable) {
        User user = getCurrentUser();
        OrderStatus orderStatus = parseOrderStatus(status);
        Page<Order> orders = orderRepository.findByUserIdAndStatus(user.getId(), orderStatus, pageable);
        return orders.map(OrderSummaryResponse::from);
    }

    @Override
    public void cancelOrder(Integer orderId, String reason) {
        User user = getCurrentUser();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(MessageKey.ORDER_NOT_FOUND));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new InvalidException(MessageKey.ORDER_ACCESS_DENIED);
        }

        if (!canCancelOrder(orderId)) {
            throw new InvalidException(MessageKey.ORDER_CANNOT_CANCEL);
        }
        // RESTORE LAI KHO HANG
        restoreStock(order);
        order.setStatus(OrderStatus.CANCELLED);
        order.setNote(order.getNote() != null ? order.getNote() + " | Cancelled: " + reason : "Cancelled: " + reason);
        orderRepository.save(order);

        log.info("Order {} cancelled by user {} with reason: {}", orderId, user.getId(), reason);
    }

    @Override
    public OrderResponse reOrder(Integer orderId) {
        User user = getCurrentUser();
        Order originalOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(MessageKey.ORDER_NOT_FOUND));

        if (!originalOrder.getUser().getId().equals(user.getId())) {
            throw new InvalidException(MessageKey.ORDER_ACCESS_DENIED);
        }

        List<OrderItemRequest> items = originalOrder.getOrderItems().stream()
                .map(item -> OrderItemRequest.builder()
                        .variantId(item.getVariant().getId())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        validateOrder(items);

        OrderRequest request = OrderRequest.builder()
                .phoneDelivery(originalOrder.getPhoneDelivery())
                .province(originalOrder.getProvince())
                .district(originalOrder.getDistrict())
                .ward(originalOrder.getWard())
                .addressDetail(originalOrder.getAddressDetail())
                .paymentMethodId(originalOrder.getPayments().stream()
                        .findFirst()
                        .map(p -> p.getPaymentMethod().getId())
                        .orElse(1))
                .build();

        Order newOrder = createOrder(user, request, items);

        // Add payment logic for re-order
        String transactionId = "ORD-" + newOrder.getOrderNumber();
        PaymentResponse paymentResponse = paymentService.create(newOrder, request.getPaymentMethodId(), transactionId);

        if (paymentResponse.getPaymentMethod().getName().equalsIgnoreCase(payos)) {
            ObjectNode response = paymentLinkService.createPaymentLink(newOrder);
            return OrderResponse.fromWithPaymentLink(newOrder, response);
        }

        return OrderResponse.from(newOrder);
    }

    // ADMIN

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrders(OrderFilterRequest filter, Pageable pageable) {
        Page<Order> orders = orderRepository.filterOrders(filter, pageable);
        return orders.map(OrderResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderByIdAdmin(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(MessageKey.ORDER_NOT_FOUND));
        return OrderResponse.from(order);
    }

    @Override
    public OrderResponse updateOrderStatus(Integer orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(MessageKey.ORDER_NOT_FOUND));

        OrderStatus newStatus = parseOrderStatus(status);
        validateStatusTransition(order.getStatus(), newStatus);
        // Confirm inventory chỉ khi chuyển từ PENDING → CONFIRMED (COD)
        if (OrderStatus.CONFIRMED.equals(newStatus) && OrderStatus.PENDING.equals(order.getStatus())) {
            for (OrderItem item : order.getOrderItems()) {
                productService.confirmInventory(item.getVariant().getId(), item.getQuantity());
            }
        }
        order.setStatus(newStatus);
        orderRepository.save(order);

        log.info("Order {} status updated to {} by admin", orderId, newStatus);
        return OrderResponse.from(order);
    }

    @Override
    public OrderResponse markAsDelivered(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(MessageKey.ORDER_NOT_FOUND));

        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new InvalidException(MessageKey.INVALID_STATUS_TRANSITION);
        }

        order.setStatus(OrderStatus.DELIVERED);
        orderRepository.save(order);

        log.info("Order {} marked as delivered", orderId);
        return OrderResponse.from(order);
    }

    @Override
    public void processRefund(Integer orderId, BigDecimal refundAmount, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(MessageKey.ORDER_NOT_FOUND));

        if (refundAmount.compareTo(order.getFinalAmount()) > 0) {
            throw new InvalidException("Refund amount cannot exceed order total");
        }

        restoreStock(order);
        order.setStatus(OrderStatus.CANCELLED);
        String refundNote = String.format("Refunded: %s VND - Reason: %s", refundAmount.toString(), reason);
        order.setNote(order.getNote() != null ? order.getNote() + " | " + refundNote : refundNote);
        orderRepository.save(order);

        log.info("Order {} refunded {} VND with reason: {}", orderId, refundAmount, reason);
    }

    @Override
    public void bulkUpdateStatus(List<Integer> orderIds, String status) {
        OrderStatus newStatus = parseOrderStatus(status);

        List<Order> orders = orderRepository.findAllById(orderIds);
        for (Order order : orders) {
            try {
                validateStatusTransition(order.getStatus(), newStatus);
                order.setStatus(newStatus);
            } catch (InvalidException e) {
                log.warn("Skipping order {} - invalid status transition", order.getId());
            }
        }

        orderRepository.saveAll(orders);
        log.info("Bulk status update to {} for {} orders", status, orders.size());
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] exportOrders(OrderFilterRequest filter, String format) {
        List<Order> orders = orderRepository.filterOrders(filter, Pageable.unpaged()).getContent();

        StringBuilder csv = new StringBuilder();
        csv.append("Order Number,Status,Total,User,Date\n");

        for (Order order : orders) {
            csv.append(String.format("%s,%s,%s,%s,%s\n",
                    order.getOrderNumber(),
                    order.getStatus().getValue(),
                    order.getFinalAmount(),
                    order.getUser().getFullName(),
                    order.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE)));
        }

        return csv.toString().getBytes();
    }

    // ORdER TRACKING

    @Override
    @Transactional(readOnly = true)
    public OrderResponse trackOrder(String orderNumber, String email) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new NotFoundException(MessageKey.ORDER_NOT_FOUND));

        if (!order.getUser().getEmail().equalsIgnoreCase(email)) {
            throw new InvalidException(MessageKey.ORDER_ACCESS_DENIED);
        }

        return OrderResponse.fromWithoutUser(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object> getOrderTimeline(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(MessageKey.ORDER_NOT_FOUND));

        List<Map<String, Object>> timeline = new ArrayList<>();

        Map<String, Object> created = new HashMap<>();
        created.put("event", "Order Created");
        created.put("status", OrderStatus.PENDING.getValue());
        created.put("timestamp", order.getCreatedAt());
        timeline.add(created);

        if (order.getStatus() != OrderStatus.PENDING) {
            Map<String, Object> current = new HashMap<>();
            current.put("event", "Status: " + order.getStatus().getValue());
            current.put("status", order.getStatus().getValue());
            current.put("timestamp", order.getUpdatedAt());
            timeline.add(current);
        }

        return new ArrayList<>(timeline);
    }

    // VALIDATION & UTILITY

    @Override
    public void validateOrder(List<OrderItemRequest> items) {
        if (items == null || items.isEmpty()) {
            throw new InvalidException(MessageKey.INVALID_INPUT);
        }

        for (OrderItemRequest item : items) {
            ProductVariant variant = productVariantRepository.findById(item.getVariantId())
                    .orElseThrow(() -> new NotFoundException(MessageKey.VARIANT_NOT_FOUND));

            if (item.getQuantity() <= 0 || item.getQuantity() > MAX_QUANTITY_PER_ITEM) {
                throw new InvalidException(MessageKey.QUANTITY_SO_BIG);
            }

            if (variant.getInventory().getQuantity() < item.getQuantity()) {
                throw new InvalidException(MessageKey.PRODUCT_NOT_ENOUGH);
            }

            if (!variant.getProduct().getIsActive()) {
                throw new InvalidException(MessageKey.PRODUCT_DOESNT_ACTIVE);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean canCancelOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException(MessageKey.ORDER_NOT_FOUND));

        return order.getStatus() == OrderStatus.PENDING ||
                order.getStatus() == OrderStatus.CONFIRMED;
    }

    @Override
    public String generateOrderNumber() {
        SecureRandom secureRandom = new SecureRandom();
        String orderNumber;
        do {
            String timePart = String.valueOf(System.currentTimeMillis() % 1_000_000);
            String randomPart = String.format("%04d", secureRandom.nextInt(10000));
            orderNumber = timePart + randomPart;
        } while (orderRepository.existsByOrderNumber(orderNumber));
        return orderNumber;
    }

    // HELpER METHOD

    private User getCurrentUser() {
        UserPrincipal userPrincipal = SecurityUtils.getCurrentUser();
        if (userPrincipal == null) {
            throw new NotFoundException(MessageKey.USER_NOT_LOGIN);
        }
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new NotFoundException(MessageKey.USER_NOT_FOUND));
    }

    private Order createOrder(User user, OrderRequest request, List<OrderItemRequest> items) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest item : items) {
            ProductVariant variant = productVariantRepository.findById(item.getVariantId())
                    .orElseThrow(() -> new NotFoundException(MessageKey.VARIANT_NOT_FOUND));

            // calculate price
            Product product = variant.getProduct();
            BigDecimal basePrice = product.getCurrentPrice();
            BigDecimal additionalPrice = variant.getAdditionalPrice() != null ? variant.getAdditionalPrice()
                    : BigDecimal.ZERO;
            BigDecimal price = basePrice.add(additionalPrice);

            BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(item.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            OrderItem orderItem = OrderItem.builder()
                    .variant(variant)
                    .quantity(item.getQuantity())
                    .unitPrice(price)
                    .build();
            orderItems.add(orderItem);
        }

        BigDecimal shippingFee = shippingService.calculateShippingFee(request.getDistrict(), request.getWard(),
                totalAmount);
        BigDecimal discountAmount = BigDecimal.ZERO;

        if (request.getDiscountCode() != null) {
            // lay discount
            Map<DiscountType, BigDecimal> discountMap = discountCalculationService.applyDiscountCode(
                    request.getDiscountCode(), user, totalAmount,
                    shippingFee);
            // tru amount hoac ship
            discountAmount = discountMap.get(DiscountType.AMOUNT);
            shippingFee = shippingFee.subtract(discountMap.get(DiscountType.SHIP));
        }

        BigDecimal finalAmount = totalAmount.add(shippingFee).subtract(discountAmount);

        Order order = Order.builder()
                .user(user)
                .orderNumber(generateOrderNumber())
                .totalAmount(totalAmount)
                .shippingFee(shippingFee)
                .discountAmount(discountAmount)
                .finalAmount(finalAmount)
                .status(OrderStatus.PENDING)
                .phoneDelivery(request.getPhoneDelivery())
                .province(request.getProvince())
                .district(request.getDistrict())
                .ward(request.getWard())
                .addressDetail(request.getAddressDetail())
                .note(request.getNote())
                .build();

        order = orderRepository.save(order);

        // record  usage if applied
        if (request.getDiscountCode() != null) {
            Discount discount = discountRepository.findByName(request.getDiscountCode())
                    .orElse(null);
            if (discount != null) {
                discountService.recordUsage(discount, user, order);
            }
        }

        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order);
            productService.reserveInventory(orderItem.getVariant().getId(), orderItem.getQuantity());
        }
        orderItemRepository.saveAll(orderItems);
        order.setOrderItems(new HashSet<>(orderItems));

        log.info("Order {} created for user {} with {} items, total: {}",
                order.getOrderNumber(), user.getId(), orderItems.size(), finalAmount);

        return order;
    }

    private void restoreStock(Order order) {
        for (OrderItem item : order.getOrderItems()) {
            productService.releaseInventory(item.getVariant().getId(), item.getQuantity());
        }
    }

    private OrderStatus parseOrderStatus(String status) {
        try {
            return OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidException(MessageKey.ORDER_INVALID_STATUS);
        }
    }

    private void validateStatusTransition(OrderStatus current, OrderStatus next) {
        Map<OrderStatus, Set<OrderStatus>> allowedTransitions = Map.of(
                OrderStatus.PENDING, Set.of(OrderStatus.CONFIRMED, OrderStatus.CANCELLED),
                OrderStatus.CONFIRMED, Set.of(OrderStatus.SHIPPED, OrderStatus.CANCELLED),
                OrderStatus.SHIPPED, Set.of(OrderStatus.DELIVERED, OrderStatus.CANCELLED),
                OrderStatus.DELIVERED, Set.of(),
                OrderStatus.CANCELLED, Set.of());

        if (!allowedTransitions.getOrDefault(current, Set.of()).contains(next)) {
            throw new InvalidException(MessageKey.INVALID_STATUS_TRANSITION);
        }
    }

}
