package com.thacbao.neki.services.impl;

import com.thacbao.neki.dto.response.PaymentResponse;
import com.thacbao.neki.enums.PaymentStatus;
import com.thacbao.neki.exceptions.common.NotFoundException;
import com.thacbao.neki.model.Order;
import com.thacbao.neki.model.OrderItem;
import com.thacbao.neki.model.Payment;
import com.thacbao.neki.model.PaymentMethod;
import com.thacbao.neki.repositories.jpa.PaymentMethodRepository;
import com.thacbao.neki.repositories.jpa.PaymentRepository;
import com.thacbao.neki.services.PaymentService;
import com.thacbao.neki.services.ProductService;
import com.thacbao.neki.utils.MessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.payos.model.webhooks.WebhookData;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final com.thacbao.neki.repositories.jpa.OrderRepository orderRepository;
    private final ProductService productService;
    private static final String PAYOS_SUCCESS_CODE = "00";

    @Override
    public PaymentResponse create(Order order, Integer paymentMethodId, String paymentLinkId) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId).orElseThrow(
                () -> new NotFoundException(MessageKey.PAYMENT_METHOD_NOT_FOUND));
        Payment payment = Payment.builder()
                .order(order)
                .paymentMethod(paymentMethod)
                .amount(order.getFinalAmount())
                .transactionId(paymentLinkId)
                .status(PaymentStatus.PENDING)
                .build();
        return PaymentResponse.from(paymentRepository.save(payment));
    }

    @Override
    public PaymentResponse update(Integer paymentId, PaymentStatus status) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(
                () -> new NotFoundException("payment not found"));
        payment.setStatus(status);
        if (status == PaymentStatus.PAID) {
            payment.setPaidAt(java.time.LocalDateTime.now());
        }
        return PaymentResponse.from(paymentRepository.save(payment));
    }

    @Override
    @Transactional
    public void handlePayOSWebhook(WebhookData data) {
        log.info("Handling PayOS Webhook for order: {}", data.getOrderCode());
        Payment payment = paymentRepository.findByOrderOrderNumber(String.valueOf(data.getOrderCode()))
                .orElseThrow(
                        () -> new NotFoundException("Payment for order code " + data.getOrderCode() + " not found"));

        if (PAYOS_SUCCESS_CODE.equals(data.getCode())) {
            payment.setStatus(PaymentStatus.PAID);
            payment.setPaidAt(java.time.LocalDateTime.now());

            Order order = payment.getOrder();
            order.setStatus(com.thacbao.neki.enums.OrderStatus.CONFIRMED);
            orderRepository.save(order);

            // Confirm inventory for all items
            for (OrderItem item : order.getOrderItems()) {
                productService.confirmInventory(item.getVariant().getId(), item.getQuantity());
            }

            log.info("Order {} confirmed and inventory updated after successful payment", order.getOrderNumber());
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            Order order = payment.getOrder();
            for (OrderItem item : order.getOrderItems()) {
                productService.restoreReserveInventory(item.getVariant().getId(), item.getQuantity());
            }
            log.warn("Payment for order {} failed with code: {}", order.getOrderNumber(), data.getCode());
        }

        paymentRepository.save(payment);
    }
}
