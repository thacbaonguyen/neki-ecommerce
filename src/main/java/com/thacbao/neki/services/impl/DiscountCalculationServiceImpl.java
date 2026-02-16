package com.thacbao.neki.services.impl;

import com.thacbao.neki.enums.DiscountType;
import com.thacbao.neki.model.Discount;
import com.thacbao.neki.model.User;
import com.thacbao.neki.services.DiscountCalculationService;
import com.thacbao.neki.services.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DiscountCalculationServiceImpl implements DiscountCalculationService {

    private final DiscountService discountService;

    @Override
    public Map<DiscountType, BigDecimal> applyDiscountCode(String discountCode, User user, BigDecimal orderAmount,
            BigDecimal shippingFee) {
        Map<DiscountType, BigDecimal> result = new HashMap<>();
        result.put(DiscountType.AMOUNT, BigDecimal.ZERO);
        result.put(DiscountType.SHIP, BigDecimal.ZERO);

        if (discountCode == null || discountCode.isEmpty()) {
            return result;
        }

        Discount discount = discountService.validateAndGetDiscount(discountCode, user, orderAmount);

        if (discount.getDiscountType().equals(DiscountType.AMOUNT)) {
            if (discount.getPercent() != null && discount.getPercent() > 0) {
                BigDecimal discountAmt = orderAmount
                        .multiply(BigDecimal.valueOf(discount.getPercent()))
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                result.put(DiscountType.AMOUNT, discountAmt);
            } else if (discount.getReduceAmount() != null
                    && discount.getReduceAmount().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal actualDiscount = discount.getReduceAmount().min(orderAmount);
                result.put(DiscountType.AMOUNT, actualDiscount);
            }
        } else {
            // DiscountType.SHIP
            if (discount.getPercent() != null && discount.getPercent() > 0) {
                BigDecimal shipDiscount = shippingFee
                        .multiply(BigDecimal.valueOf(discount.getPercent()))
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                result.put(DiscountType.SHIP, shipDiscount);
            } else if (discount.getReduceAmount() != null
                    && discount.getReduceAmount().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal actualDiscount = discount.getReduceAmount().min(shippingFee);
                result.put(DiscountType.SHIP, actualDiscount);
            }
        }
        return result;
    }
}
