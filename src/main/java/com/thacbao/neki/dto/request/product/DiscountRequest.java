package com.thacbao.neki.dto.request.product;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRequest {
    @NotBlank(message = "Ten ma giam gia khong duoc de trong")
    private String name;

    @NotBlank(message = "Ma giam gia khong duoc de trong")
    private String code;

    private Integer percent;

    private BigDecimal reduceAmount;

    @NotBlank(message = "loai ma giam gia khong duoc de trong")
    private String discountType;

    @NotBlank(message = "mo ta khong duoc de trong")
    private String description;

    @com.fasterxml.jackson.annotation.JsonProperty("isActive")
    private boolean active;

    private Integer usageLimit;
    private Integer userUsageLimit;
    private java.math.BigDecimal minOrderAmount;
    private java.time.LocalDate startDate;
    private java.time.LocalDate endDate;
}
