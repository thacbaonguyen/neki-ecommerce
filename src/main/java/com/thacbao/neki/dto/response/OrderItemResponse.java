package com.thacbao.neki.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.thacbao.neki.model.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemResponse {
    private Integer id;
    private ProductVariantResponse variant;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    public static OrderItemResponse from(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .id(orderItem.getId())
                .variant(ProductVariantResponse.from(orderItem.getVariant(), orderItem.getUnitPrice()))
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .totalPrice(orderItem.getTotalPrice())
                .build();
    }
}
