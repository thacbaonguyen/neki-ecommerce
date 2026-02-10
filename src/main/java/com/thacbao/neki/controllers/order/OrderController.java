//package com.thacbao.neki.controllers.order;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import com.thacbao.neki.dto.request.product.OrderRequest;
//import com.thacbao.neki.dto.response.OrderResponse;
//import com.thacbao.neki.services.OrderService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import vn.payos.PayOS;
//import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
//import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
//import vn.payos.model.v2.paymentRequests.PaymentLink;
//import vn.payos.model.v2.paymentRequests.PaymentLinkItem;
//import vn.payos.model.webhooks.ConfirmWebhookResponse;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/v1/order")
//@RequiredArgsConstructor
//public class OrderController {
//    private final PayOS payOS;
//    private final OrderService orderService;
//
//    @PostMapping(path = "/create")
//    public ObjectNode createPaymentLink(@RequestBody OrderRequest request) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        ObjectNode response = objectMapper.createObjectNode();
//        try {
//            OrderResponse orderResponse = orderService.createOrderFromCart();
//            Order order = orderService.createOrder();
//            orderDetailService.createOrderDetail(order);
//            final String productName = request.getProductName();
//            final String description = request.getDescription();
//            final String returnUrl = request.getReturnUrl();
//            final String cancelUrl = request.getCancelUrl();
//            final Long price = (long) order.getTotalAmount();
//            // Gen order code
//            String currentTimeString = String.valueOf(String.valueOf(new Date().getTime()));
//            long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));
//
//            PaymentLinkItem item = PaymentLinkItem.builder()
//                    .name(productName)
//                    .price(price)  // price là long
//                    .quantity(1)
//                    .build();
//
//            CreatePaymentLinkRequest paymentRequest = CreatePaymentLinkRequest.builder()
//                    .orderCode(orderCode)
//                    .amount(price)
//                    .description(description)
//                    .items(List.of(item))  // List<Item>
//                    .returnUrl(returnUrl)
//                    .cancelUrl(cancelUrl)
//                    .build();
//
//            CreatePaymentLinkResponse apiResponse = payOS.paymentRequests().create(paymentRequest);
//            order.setOrderCode(String.valueOf(apiResponse.getOrderCode()));
//            orderService.saveOrder(order);
//
//            response.put("error", 0);
//            response.put("message", "success");
//            response.set("data", objectMapper.valueToTree(apiResponse));
//            return response;
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.put("error", -1);
//            response.put("message", "fail");
//            response.set("data", null);
//            return response;
//
//        }
//    }
//
//    @GetMapping(path = "/{orderId}")
//    public ObjectNode getOrderById(@PathVariable("orderId") long orderId) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        ObjectNode response = objectMapper.createObjectNode();
//
//        try {
//            PaymentLink order = payOS.paymentRequests().get(orderId);
//
//            response.set("data", objectMapper.valueToTree(order));
//            response.put("error", 0);
//            response.put("message", "ok");
//            return response;
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.put("error", -1);
//            response.put("message", e.getMessage());
//            response.set("data", null);
//            return response;
//        }
//    }
//
//    @PutMapping(path = "/{orderId}")
//    public ObjectNode cancelOrder(@PathVariable("orderId") int orderId) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        ObjectNode response = objectMapper.createObjectNode();
//        try {
//            PaymentLink order = payOS.paymentRequests().cancel((long) orderId, "reason if any");
//            response.set("data", objectMapper.valueToTree(order));
//            response.put("error", 0);
//            response.put("message", "ok");
//            return response;
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.put("error", -1);
//            response.put("message", e.getMessage());
//            response.set("data", null);
//            return response;
//        }
//    }
//
//    @PostMapping(path = "/confirm-webhook")
//    public ObjectNode confirmWebhook(@RequestBody Map<String, String> requestBody) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        ObjectNode response = objectMapper.createObjectNode();
//        try {
//            String webhookUrl = requestBody.get("webhookUrl");
//            if (webhookUrl == null || webhookUrl.isEmpty()) {
//                throw new IllegalArgumentException("webhookUrl is required");
//            }
//            ConfirmWebhookResponse result = payOS.webhooks().confirm(webhookUrl);
//            response.set("data", objectMapper.valueToTree(result));
//            response.put("error", 0);
//            response.put("message", "ok");
//            return response;
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.put("error", -1);
//            response.put("message", e.getMessage());
//            response.set("data", null);
//            return response;
//        }
//    }
//
//    @PutMapping("/update-status")
//    public ResponseEntity<ApiResponse> updateStatusOrder(@RequestBody OrderConfirmRequest request){
//        return orderService.updateStatus(request);
//    }
//}