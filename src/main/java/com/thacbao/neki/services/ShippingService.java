package com.thacbao.neki.services;

import java.math.BigDecimal;

public interface ShippingService {
    BigDecimal calculateShippingFee(String district, String ward, BigDecimal orderAmount);
}
