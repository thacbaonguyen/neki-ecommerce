package com.thacbao.neki.dto.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thacbao.neki.model.ProductVariant;
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
public class ProductVariantResponse {
    private Integer id;
    private ColorResponse color;
    private SizeResponse size;
    private BigDecimal additionalPrice;
    private BigDecimal finalPrice;
    private Boolean isActive;
    private Integer quantity;
    private Integer reservedQuantity;
    private Boolean inStock;

    public static ProductVariantResponse from(ProductVariant variant, BigDecimal basePrice) {
        Integer quantity = variant.getInventory() != null ? variant.getInventory().getQuantity() : 0;
        Integer reserved = variant.getInventory() != null ? variant.getInventory().getReservedQuantity() : 0;

        return ProductVariantResponse.builder()
                .id(variant.getId())
                .color(ColorResponse.from(variant.getColor()))
                .size(SizeResponse.from(variant.getSize()))
                .additionalPrice(variant.getAdditionalPrice())
                .finalPrice(basePrice.add(variant.getAdditionalPrice()))
                .isActive(variant.getIsActive())
                .quantity(quantity)
                .reservedQuantity(reserved)
                .inStock(quantity > reserved)
                .build();
    }
}