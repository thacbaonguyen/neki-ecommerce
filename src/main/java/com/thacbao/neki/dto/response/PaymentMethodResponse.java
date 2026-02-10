package com.thacbao.neki.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.thacbao.neki.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentMethodResponse {
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime createAt;

    public static PaymentMethodResponse from(PaymentMethod paymentMethod) {
        return PaymentMethodResponse.builder()
                .id(paymentMethod.getId())
                .name(paymentMethod.getName())
                .description(paymentMethod.getDescription())
                .createAt(LocalDateTime.now())
                .build();
    }
}
