package com.thacbao.neki.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.thacbao.neki.enums.PaymentStatus;
import com.thacbao.neki.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentResponse {
    private Integer id;
    private BigDecimal amount;
    private String transactionId;
    private PaymentStatus status;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
    private PaymentMethodResponse paymentMethod;

    public static PaymentResponse from(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .amount(payment.getAmount())
                .transactionId(payment.getTransactionId())
                .status(payment.getStatus())
                .paidAt(payment.getPaidAt())
                .createdAt(payment.getCreatedAt())
                .paymentMethod(PaymentMethodResponse.from(payment.getPaymentMethod()))
                .build();
    }
}
