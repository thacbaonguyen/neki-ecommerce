package com.thacbao.neki.controllers.pub;

import com.thacbao.neki.dto.response.ApiResponse;
import com.thacbao.neki.dto.response.DiscountResponse;
import com.thacbao.neki.services.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/discount")
public class DiscountController {
    private final DiscountService discountService;

    @GetMapping()
    public ResponseEntity<ApiResponse<List<DiscountResponse>>> discount(@RequestParam String discountType) {
        List<DiscountResponse> responses = discountService.getAllByType(discountType);
        return ResponseEntity.ok(
                ApiResponse.<List<DiscountResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(responses)
                        .build()
        );
    }
}
