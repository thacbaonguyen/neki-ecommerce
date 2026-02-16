package com.thacbao.neki.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thacbao.neki.model.Order;
import com.thacbao.neki.services.PaymentLinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.v2.paymentRequests.PaymentLinkItem;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentLinkServiceImpl implements PaymentLinkService {
    private final PayOS payOS;

    @Value("${URL.returnUrl}")
    private String returnUrl;

    @Value("${URL.cancelUrl}")
    private String cancelUrl;
    @Override
    public ObjectNode createPaymentLink(Order order) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            List<PaymentLinkItem> paymentLinkItems = order.getOrderItems().stream()
                    .map(item -> PaymentLinkItem.builder()
                            .name(item.getVariant().getProduct().getName())
                            .price(item.getUnitPrice().longValue())
                            .quantity(item.getQuantity())
                            .build())
                    .collect(Collectors.toList());

            long totalAmount = order.getFinalAmount().longValue();

            CreatePaymentLinkRequest paymentLinkRequest = CreatePaymentLinkRequest.builder()
                    .orderCode(Long.parseLong(order.getOrderNumber()))
                    .amount(totalAmount)
                    .description("Order :" + order.getOrderNumber())
                    .items(paymentLinkItems)
                    .buyerPhone(order.getPhoneDelivery())
                    .buyerAddress(order.getAddressDetail() + ", " + order.getWard() + ", " + order.getDistrict() + ", "
                            + order.getProvince())
                    .returnUrl(returnUrl)
                    .cancelUrl(cancelUrl)
                    .expiredAt(1000 * 60 * 10L)
                    .build();

            CreatePaymentLinkResponse paymentLinkResponse = payOS.paymentRequests().create(paymentLinkRequest);
            response.put("error", 0);
            response.put("message", "success");
            response.set("data", objectMapper.valueToTree(paymentLinkResponse));
            return response;
        } catch (Exception e) {
            response.put("error", -1);
            response.put("message", "fail");
            response.set("data", null);
            return response;
        }

    }
}
