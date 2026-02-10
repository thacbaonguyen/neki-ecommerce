package com.thacbao.neki.controllers.admin;

import com.thacbao.neki.dto.request.product.PaymentMethodRequest;
import com.thacbao.neki.dto.response.ApiResponse;
import com.thacbao.neki.services.PaymentMethodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/payment-method")
@RequiredArgsConstructor
public class AdminPaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Void>> createPaymentMethod(@Valid @RequestBody PaymentMethodRequest request){
        paymentMethodService.create(request);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Tao thành cồng  method")
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> togglePaymentMethod(@PathVariable Integer id, boolean status){
        paymentMethodService.update(id, status);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Update status thành cồng  method")
                        .build()
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> deletePaymentMethod(@PathVariable Integer id){
        paymentMethodService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Xoa thành cồng  method")
                        .build()
        );
    }
}
