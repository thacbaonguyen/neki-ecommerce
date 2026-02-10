package com.thacbao.neki.controllers.order;

import com.thacbao.neki.dto.response.ApiResponse;
import com.thacbao.neki.dto.response.PaymentMethodResponse;
import com.thacbao.neki.services.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment-method")
@RequiredArgsConstructor
public class PaymentMethodController {
    private final PaymentMethodService paymentMethodService;



    @GetMapping()
    public ResponseEntity<ApiResponse<List<PaymentMethodResponse>>> getPaymentMethod(){
        List<PaymentMethodResponse> responses = paymentMethodService.getAll();
        return ResponseEntity.ok(
                ApiResponse.<List<PaymentMethodResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(responses)
                        .build()
        );
    }


}
