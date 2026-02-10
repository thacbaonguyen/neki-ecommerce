package com.thacbao.neki.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thacbao.neki.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {
        private Integer id;
        private String orderNumber;
        private UserResponseDTO user;
        private BigDecimal totalAmount;
        private BigDecimal shippingFee;
        private BigDecimal discountAmount;
        private BigDecimal finalAmount;
        private String status;
        private String phoneDelivery;
        private String province;
        private String district;
        private String ward;
        private String addressDetail;
        private String note;
        private Set<OrderItemResponse> orderItems;
        private Set<PaymentResponse> payments;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private ObjectNode paymentLink;

        public static OrderResponse from(Order order) {
                return OrderResponse.builder()
                                .id(order.getId())
                                .orderNumber(order.getOrderNumber())
                                .user(UserResponseDTO.from(order.getUser()))
                                .totalAmount(order.getTotalAmount())
                                .shippingFee(order.getShippingFee())
                                .discountAmount(order.getDiscountAmount())
                                .finalAmount(order.getFinalAmount())
                                .status(order.getStatus() != null ? order.getStatus().getValue() : null)
                                .phoneDelivery(order.getPhoneDelivery())
                        .province(order.getProvince())
                                .district(order.getDistrict())
                                .ward(order.getWard())
                                .addressDetail(order.getAddressDetail())
                                .note(order.getNote())
                                .orderItems(order.getOrderItems().stream()
                                                .map(OrderItemResponse::from)
                                                .collect(Collectors.toSet()))
                        .payments(order.getPayments().stream().map(PaymentResponse::from).collect(Collectors.toSet()))
                                .createdAt(order.getCreatedAt())
                                .updatedAt(order.getUpdatedAt())
                                .build();
        }

        public static OrderResponse fromWithPaymentLink(Order order, ObjectNode paymentLink) {
                return OrderResponse.builder()
                        .id(order.getId())
                        .orderNumber(order.getOrderNumber())
                        .user(UserResponseDTO.from(order.getUser()))
                        .totalAmount(order.getTotalAmount())
                        .shippingFee(order.getShippingFee())
                        .discountAmount(order.getDiscountAmount())
                        .finalAmount(order.getFinalAmount())
                        .status(order.getStatus() != null ? order.getStatus().getValue() : null)
                        .phoneDelivery(order.getPhoneDelivery())
                        .province(order.getProvince())
                        .district(order.getDistrict())
                        .ward(order.getWard())
                        .addressDetail(order.getAddressDetail())
                        .note(order.getNote())
                        .orderItems(order.getOrderItems().stream()
                                .map(OrderItemResponse::from)
                                .collect(Collectors.toSet()))
                        .payments(order.getPayments().stream().map(PaymentResponse::from).collect(Collectors.toSet()))
                        .paymentLink(paymentLink)
                        .createdAt(order.getCreatedAt())
                        .updatedAt(order.getUpdatedAt())
                        .build();
        }

        public static OrderResponse fromWithoutUser(Order order) {
                return OrderResponse.builder()
                                .id(order.getId())
                                .orderNumber(order.getOrderNumber())
                                .totalAmount(order.getTotalAmount())
                                .shippingFee(order.getShippingFee())
                                .discountAmount(order.getDiscountAmount())
                                .finalAmount(order.getFinalAmount())
                                .status(order.getStatus() != null ? order.getStatus().getValue() : null)
                                .phoneDelivery(order.getPhoneDelivery())
                        .province(order.getProvince())
                                .district(order.getDistrict())
                                .ward(order.getWard())
                                .addressDetail(order.getAddressDetail())
                                .note(order.getNote())
                                .orderItems(order.getOrderItems().stream()
                                                .map(OrderItemResponse::from)
                                                .collect(Collectors.toSet()))
                        .payments(order.getPayments().stream().map(PaymentResponse::from).collect(Collectors.toSet()))
                                .createdAt(order.getCreatedAt())
                                .updatedAt(order.getUpdatedAt())
                                .build();
        }
}
