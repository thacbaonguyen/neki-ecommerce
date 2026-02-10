package com.thacbao.neki.model;

import com.thacbao.neki.enums.DiscountType;
import com.thacbao.neki.model.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "discounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Discount extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Column
    private Integer percent;

    @Column(name = "reduce_amount")
    private BigDecimal reduceAmount;

    @Column(nullable = false)
    private String description;

    @Builder.Default
    @Column(name = "discount_type",nullable = false)
    private DiscountType discountType = DiscountType.AMOUNT;

    @Column(name = "is_active")
    @Builder.Default
    private boolean isActive = true;
}
