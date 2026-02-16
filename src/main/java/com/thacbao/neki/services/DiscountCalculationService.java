package com.thacbao.neki.services;

import com.thacbao.neki.enums.DiscountType;
import com.thacbao.neki.model.User;

import java.math.BigDecimal;
import java.util.Map;

public interface DiscountCalculationService {

    Map<DiscountType, BigDecimal> applyDiscountCode(String discountCode, User user, BigDecimal orderAmount,
            BigDecimal shippingFee);

}
