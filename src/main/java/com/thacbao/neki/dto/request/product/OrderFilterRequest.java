package com.thacbao.neki.dto.request.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderFilterRequest {

    // Search by order number or user email/fullName
    private String keyword;

    // fikter by user
    private Integer userId;

    // filter by price range
    private BigDecimal minAmount;
    private BigDecimal maxAmount;

    // filter by date range
    private LocalDate startDate;
    private LocalDate endDate;

    // filter by district/ward
    private String district;
    private String ward;
}
