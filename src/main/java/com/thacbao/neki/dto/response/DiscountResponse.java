package com.thacbao.neki.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.thacbao.neki.model.Discount;
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
public class DiscountResponse {
    private Integer id;
    private String name;
    private String description;
    private String discountType;
    private Integer percent;
    private BigDecimal reduceAmount;
    private LocalDateTime createAt;

    public static DiscountResponse from(Discount discount) {
        return DiscountResponse.builder()
                .id(discount.getId())
                .name(discount.getName())
                .description(discount.getDescription())
                .discountType(discount.getDiscountType().getValue())
                .percent(discount.getPercent())
                .reduceAmount(discount.getReduceAmount())
                .createAt(discount.getCreatedAt())
                .build();
    }
}
