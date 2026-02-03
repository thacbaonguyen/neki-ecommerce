package com.thacbao.neki.controllers.admin;

import com.thacbao.neki.dto.request.product.ProductFilterRequest;
import com.thacbao.neki.dto.request.product.ProductImageRequest;
import com.thacbao.neki.dto.request.product.ProductRequest;
import com.thacbao.neki.dto.request.product.ProductVariantRequest;
import com.thacbao.neki.dto.response.*;
import com.thacbao.neki.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductDetailResponse>> createProduct(
            @Valid @RequestBody ProductRequest request) {
        ProductDetailResponse product = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<ProductDetailResponse>builder()
                        .code(201)
                        .status("success")
                        .message("Tạo sản phẩm thành công")
                        .data(product)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> updateProduct(
            @PathVariable Integer id,
            @Valid @RequestBody ProductRequest request) {
        ProductDetailResponse product = productService.updateProduct(id, request);
        return ResponseEntity.ok(
                ApiResponse.<ProductDetailResponse>builder()
                        .code(200)
                        .status("success")
                        .message("Cập nhật sản phẩm thành công")
                        .data(product)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Xóa sản phẩm thành công")
                        .build()
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> toggleProductStatus(
            @PathVariable Integer id,
            @RequestParam boolean isActive) {
        productService.toggleProductStatus(id, isActive);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message(isActive ? "Kích hoạt sản phẩm thành công" : "Vô hiệu hóa sản phẩm thành công")
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDetailResponse>> getProductById(@PathVariable Integer id) {
        ProductDetailResponse product = productService.getProductByIdAdmin(id);
        return ResponseEntity.ok(
                ApiResponse.<ProductDetailResponse>builder()
                        .code(200)
                        .status("success")
                        .data(product)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductListResponse>>> getAllProducts(
            ProductFilterRequest filter,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProductListResponse> products = productService.getAllProductsAdmin(filter, pageable);
        return ResponseEntity.ok(
                ApiResponse.<Page<ProductListResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(products)
                        .build()
        );
    }

    @PostMapping("/{productId}/images")
    public ResponseEntity<ApiResponse<ProductImageResponse>> addProductImage(
            @PathVariable Integer productId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) Integer colorId,
            @RequestParam(required = false, defaultValue = "0") Integer displayOrder,
            @RequestParam(required = false, defaultValue = "false") Boolean isPrimary) {

        ProductImageResponse image = productService.addProductImage(
                productId, file, colorId, displayOrder, isPrimary
        );

        return ResponseEntity.ok(
                ApiResponse.<ProductImageResponse>builder()
                        .code(200)
                        .status("success")
                        .message("Thêm ảnh thành công")
                        .data(image)
                        .build()
        );
    }

    @PostMapping("/{productId}/images/url")
    public ResponseEntity<ApiResponse<ProductImageResponse>> addProductImageByUrl(
            @PathVariable Integer productId,
            @Valid @RequestBody ProductImageRequest request) {

        ProductImageResponse image = productService.addProductImageByUrl(productId, request);

        return ResponseEntity.ok(
                ApiResponse.<ProductImageResponse>builder()
                        .code(200)
                        .status("success")
                        .message("Thêm ảnh thành công")
                        .data(image)
                        .build()
        );
    }

    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<ApiResponse<Void>> deleteProductImage(@PathVariable Integer imageId) {
        productService.deleteProductImage(imageId);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Xóa ảnh thành công")
                        .build()
        );
    }

    @PutMapping("/{productId}/images/reorder")
    public ResponseEntity<ApiResponse<Void>> updateImageOrder(
            @PathVariable Integer productId,
            @RequestBody List<Integer> imageIds) {
        productService.updateImageOrder(productId, imageIds);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Sắp xếp lại ảnh thành công")
                        .build()
        );
    }

    @PatchMapping("/images/{imageId}/primary")
    public ResponseEntity<ApiResponse<Void>> setPrimaryImage(@PathVariable Integer imageId) {
        productService.setPrimaryImage(imageId);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Đặt ảnh chính thành công")
                        .build()
        );
    }

    // Product Variants

    @PostMapping("/{productId}/variants")
    public ResponseEntity<ApiResponse<ProductVariantResponse>> addVariant(
            @PathVariable Integer productId,
            @Valid @RequestBody ProductVariantRequest request) {

        ProductVariantResponse variant = productService.addProductVariant(productId, request);

        return ResponseEntity.ok(
                ApiResponse.<ProductVariantResponse>builder()
                        .code(200)
                        .status("success")
                        .message("Thêm phiên bản sản phẩm thành công")
                        .data(variant)
                        .build()
        );
    }

    @PutMapping("/variants/{variantId}")
    public ResponseEntity<ApiResponse<ProductVariantResponse>> updateVariant(
            @PathVariable Integer variantId,
            @Valid @RequestBody ProductVariantRequest request) {

        ProductVariantResponse variant = productService.updateProductVariant(variantId, request);

        return ResponseEntity.ok(
                ApiResponse.<ProductVariantResponse>builder()
                        .code(200)
                        .status("success")
                        .message("Cập nhật phiên bản sản phẩm thành công")
                        .data(variant)
                        .build()
        );
    }

    @DeleteMapping("/variants/{variantId}")
    public ResponseEntity<ApiResponse<Void>> deleteVariant(@PathVariable Integer variantId) {
        productService.deleteProductVariant(variantId);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Xóa phiên bản sản phẩm thành công")
                        .build()
        );
    }

    @PatchMapping("/variants/{variantId}/status")
    public ResponseEntity<ApiResponse<Void>> toggleVariantStatus(
            @PathVariable Integer variantId,
            @RequestParam boolean isActive) {
        productService.toggleVariantStatus(variantId, isActive);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message(isActive ? "Kích hoạt phiên bản thành công" : "Vô hiệu hóa phiên bản thành công")
                        .build()
        );
    }

    @GetMapping("/{productId}/variants")
    public ResponseEntity<ApiResponse<List<ProductVariantResponse>>> getProductVariants(
            @PathVariable Integer productId) {
        List<ProductVariantResponse> variants = productService.getProductVariants(productId);
        return ResponseEntity.ok(
                ApiResponse.<List<ProductVariantResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(variants)
                        .build()
        );
    }

    // Inventory

    @PutMapping("/variants/{variantId}/inventory")
    public ResponseEntity<ApiResponse<Void>> updateInventory(
            @PathVariable Integer variantId,
            @RequestParam Integer quantity) {
        productService.updateInventory(variantId, quantity);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Cập nhật tồn kho thành công")
                        .build()
        );
    }

    @PostMapping("/variants/{variantId}/inventory/adjust")
    public ResponseEntity<ApiResponse<Void>> adjustInventory(
            @PathVariable Integer variantId,
            @RequestParam Integer quantity,
            @RequestParam String reason) {
        productService.adjustInventory(variantId, quantity, reason);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Điều chỉnh tồn kho thành công")
                        .build()
        );
    }

    //  Bulk

    @PatchMapping("/bulk/status")
    public ResponseEntity<ApiResponse<Void>> bulkUpdateStatus(
            @RequestBody List<Integer> productIds,
            @RequestParam boolean isActive) {
        productService.bulkUpdateStatus(productIds, isActive);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Cập nhật trạng thái hàng loạt thành công")
                        .build()
        );
    }

    @DeleteMapping("/bulk")
    public ResponseEntity<ApiResponse<Void>> bulkDelete(@RequestBody List<Integer> productIds) {
        productService.bulkDelete(productIds);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Xóa hàng loạt thành công")
                        .build()
        );
    }
}