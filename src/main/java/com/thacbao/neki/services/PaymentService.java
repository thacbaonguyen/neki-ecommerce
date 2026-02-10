package com.thacbao.neki.services;

import com.thacbao.neki.dto.response.PaymentResponse;
import com.thacbao.neki.enums.PaymentStatus;
import com.thacbao.neki.model.Order;

public interface PaymentService {
    PaymentResponse create(Order order, Integer paymentMethodId, String paymentLinkId);

    PaymentResponse update(Integer paymentId, PaymentStatus status);

    void handlePayOSWebhook(vn.payos.model.webhooks.WebhookData data);
}
