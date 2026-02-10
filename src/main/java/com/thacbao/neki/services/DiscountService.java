package com.thacbao.neki.services;

import com.thacbao.neki.dto.request.product.DiscountRequest;
import com.thacbao.neki.dto.response.DiscountResponse;

import java.util.List;

public interface DiscountService {
    void create(DiscountRequest request);
    void update(Integer id, DiscountRequest request);
    void delete(Integer id);

    List<DiscountResponse> getAllByType(String discountType);
}
