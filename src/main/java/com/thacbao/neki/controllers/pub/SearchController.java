package com.thacbao.neki.controllers.pub;

import com.thacbao.neki.dto.response.*;
import com.thacbao.neki.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
class SearchController {

    private final ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<Page<ProductListResponse>>> searchProducts(
            @RequestParam String q,
            @PageableDefault(size = 20) Pageable pageable) {

        Page<ProductListResponse> products = productService.searchProducts(q, pageable);

        return ResponseEntity.ok(
                ApiResponse.<Page<ProductListResponse>>builder()
                        .code(200)
                        .status("success")
                        .message("Tìm kiếm: \"" + q + "\"")
                        .data(products)
                        .build()
        );
    }
}