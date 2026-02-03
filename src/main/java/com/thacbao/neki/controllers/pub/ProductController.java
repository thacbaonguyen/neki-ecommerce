package com.thacbao.neki.controllers.pub;

import com.thacbao.neki.dto.request.product.ProductFilterRequest;
import com.thacbao.neki.dto.response.*;
import com.thacbao.neki.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductListResponse>>> filterProducts(
            ProductFilterRequest filter,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<ProductListResponse> products = productService.filterProducts(filter, pageable);

        return ResponseEntity.ok(
                ApiResponse.<Page<ProductListResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(products)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getProductById(@PathVariable Integer id) {
        ProductDetailResponse product = productService.getProductById(id);
        return ResponseEntity.ok(
                ApiResponse.<ProductDetailResponse>builder()
                        .code(200)
                        .status("success")
                        .data(product)
                        .build()
        );
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getProductBySlug(@PathVariable String slug) {
        ProductDetailResponse product = productService.getProductBySlug(slug);
        return ResponseEntity.ok(
                ApiResponse.<ProductDetailResponse>builder()
                        .code(200)
                        .status("success")
                        .data(product)
                        .build()
        );
    }

    @GetMapping("/{id}/related")
    public ResponseEntity<ApiResponse<List<ProductListResponse>>> getRelatedProducts(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "8") int limit) {

        List<ProductListResponse> products = productService.getRelatedProducts(id, limit);

        return ResponseEntity.ok(
                ApiResponse.<List<ProductListResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(products)
                        .build()
        );
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<Page<ProductListResponse>>> getFeaturedProducts(
            @PageableDefault(size = 20) Pageable pageable) {

        Page<ProductListResponse> products = productService.getFeaturedProducts(pageable);

        return ResponseEntity.ok(
                ApiResponse.<Page<ProductListResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(products)
                        .build()
        );
    }

    @GetMapping("/new")
    public ResponseEntity<ApiResponse<Page<ProductListResponse>>> getNewProducts(
            @PageableDefault(size = 20) Pageable pageable) {

        Page<ProductListResponse> products = productService.getNewProducts(pageable);

        return ResponseEntity.ok(
                ApiResponse.<Page<ProductListResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(products)
                        .build()
        );
    }

    @GetMapping("/sale")
    public ResponseEntity<ApiResponse<Page<ProductListResponse>>> getOnSaleProducts(
            @PageableDefault(size = 20) Pageable pageable) {

        Page<ProductListResponse> products = productService.getOnSaleProducts(pageable);

        return ResponseEntity.ok(
                ApiResponse.<Page<ProductListResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(products)
                        .build()
        );
    }

    @GetMapping("/best-sellers")
    public ResponseEntity<ApiResponse<Page<ProductListResponse>>> getBestSellers(
            @PageableDefault(size = 20) Pageable pageable) {

        Page<ProductListResponse> products = productService.getBestSellers(pageable);

        return ResponseEntity.ok(
                ApiResponse.<Page<ProductListResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(products)
                        .build()
        );
    }

    @GetMapping("/filters")
    public ResponseEntity<ApiResponse<FilterOptionsResponse>> getFilterOptions(
            @RequestParam(required = false) Integer categoryId) {

        FilterOptionsResponse options = productService.getFilterOptions(categoryId);

        return ResponseEntity.ok(
                ApiResponse.<FilterOptionsResponse>builder()
                        .code(200)
                        .status("success")
                        .data(options)
                        .build()
        );
    }
}