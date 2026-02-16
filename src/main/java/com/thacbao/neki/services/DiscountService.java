package com.thacbao.neki.services;

import com.thacbao.neki.dto.request.product.DiscountRequest;
import com.thacbao.neki.dto.response.DiscountResponse;
import com.thacbao.neki.model.Discount;
import com.thacbao.neki.model.Order;
import com.thacbao.neki.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface DiscountService {
    void create(DiscountRequest request);

    void update(Integer id, DiscountRequest request);

    void delete(Integer id);

    List<DiscountResponse> getAllByType(String discountType);

    com.thacbao.neki.model.Discount validateAndGetDiscount(String code, User user,
            BigDecimal orderAmount);

    void recordUsage(Discount discount, User user,
                     Order order);
}
