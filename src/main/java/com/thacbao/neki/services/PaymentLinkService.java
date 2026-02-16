package com.thacbao.neki.services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thacbao.neki.model.Order;

public interface PaymentLinkService {
    ObjectNode createPaymentLink(Order order);
}
