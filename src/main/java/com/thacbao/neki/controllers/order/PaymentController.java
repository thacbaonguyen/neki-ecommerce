package com.thacbao.neki.controllers.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.thacbao.neki.services.PaymentService;
import vn.payos.PayOS;
import vn.payos.model.webhooks.Webhook;
import vn.payos.model.webhooks.WebhookData;

@RestController
@RequestMapping("/payment")
@Slf4j
public class PaymentController {
    private final PayOS payOS;
    private final PaymentService paymentService;

    public PaymentController(PayOS payOS, PaymentService paymentService) {
        this.payOS = payOS;
        this.paymentService = paymentService;
    }

    @PostMapping(path = "/payos_transfer_handler")
    public ObjectNode payosTransferHandler(@RequestBody ObjectNode body)
            throws JsonProcessingException, IllegalArgumentException {
        log.info("Payos transfer handler");
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        Webhook webhookBody = objectMapper.treeToValue(body, Webhook.class);

        try {
            // Init Response
            response.put("error", 0);
            response.put("message", "Webhook delivered");
            response.set("data", null);

            WebhookData data = payOS.webhooks().verify(webhookBody);
            paymentService.handlePayOSWebhook(data);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }
}