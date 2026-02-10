package com.thacbao.neki.controllers.admin;

import com.thacbao.neki.dto.request.product.DiscountRequest;
import com.thacbao.neki.dto.response.ApiResponse;
import com.thacbao.neki.services.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/aip/v1/admin/discount")
public class AdminDiscountController {
    private final DiscountService discountService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Void>> createDiscount(@RequestBody DiscountRequest request) {
        discountService.create(request);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Tạo dícount thành công ")
                        .build()
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Void>> updateDiscount(@PathVariable("id") Integer id, @RequestBody DiscountRequest request) {
        discountService.update(id, request);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Update díscount thành công ")
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDiscount(@PathVariable("id") Integer id) {
        discountService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Delete díscount thành công ")
                        .build()
        );
    }
}
