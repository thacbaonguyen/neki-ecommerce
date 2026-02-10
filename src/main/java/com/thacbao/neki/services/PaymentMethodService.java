package com.thacbao.neki.services;

import com.thacbao.neki.dto.request.product.PaymentMethodRequest;
import com.thacbao.neki.dto.response.PaymentMethodResponse;

import java.util.List;

public interface PaymentMethodService {
    void create(PaymentMethodRequest request);
    List<PaymentMethodResponse> getAll();
    void update(Integer id, boolean status);
    void delete(Integer id);
}
