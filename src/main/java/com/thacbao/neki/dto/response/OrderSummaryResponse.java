package com.thacbao.neki.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.thacbao.neki.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderSummaryResponse {
    private Integer id;
    private String orderNumber;
    private BigDecimal finalAmount;
    private Integer totalItems;
    private LocalDateTime createdAt;

    public static OrderSummaryResponse from(Order order) {
        return OrderSummaryResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .finalAmount(order.getFinalAmount())
                .totalItems(order.getOrderItems().size())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
