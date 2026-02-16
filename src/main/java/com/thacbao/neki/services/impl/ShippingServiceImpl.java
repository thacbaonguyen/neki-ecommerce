package com.thacbao.neki.services.impl;

import com.thacbao.neki.services.ShippingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ShippingServiceImpl implements ShippingService {
    private static final BigDecimal FREE_SHIPPING_THRESHOLD = new BigDecimal("500000");
    private static final BigDecimal STANDARD_SHIPPING_FEE = new BigDecimal("30000");

    @Override
    public BigDecimal calculateShippingFee(String district, String ward, BigDecimal orderAmount) {
        if (orderAmount.compareTo(FREE_SHIPPING_THRESHOLD) >= 0) {
            return BigDecimal.ZERO;
        }
        return STANDARD_SHIPPING_FEE;
    }
}
