package com.thacbao.neki.services;

import com.thacbao.neki.dto.request.product.ProductFilterRequest;
import com.thacbao.neki.dto.request.product.ProductImageRequest;
import com.thacbao.neki.dto.request.product.ProductRequest;
import com.thacbao.neki.dto.request.product.ProductVariantRequest;
import com.thacbao.neki.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    // CRUD
    ProductDetailResponse createProduct(ProductRequest request);
    ProductDetailResponse updateProduct(Integer id, ProductRequest request);
    void deleteProduct(Integer id);
    void toggleProductStatus(Integer id, boolean isActive);
    ProductDetailResponse getProductByIdAdmin(Integer id);
    Page<ProductListResponse> getAllProductsAdmin(ProductFilterRequest filter, Pageable pageable);

    // Images
    ProductImageResponse addProductImage(Integer productId, MultipartFile file, Integer colorId, Integer displayOrder, Boolean isPrimary);
    ProductImageResponse addProductImageByUrl(Integer productId, ProductImageRequest request);
    void deleteProductImage(Integer imageId);
    void updateImageOrder(Integer productId, List<Integer> imageIds);
    void setPrimaryImage(Integer imageId);

    // variant
    ProductVariantResponse addProductVariant(Integer productId, ProductVariantRequest request);
    ProductVariantResponse updateProductVariant(Integer variantId, ProductVariantRequest request);
    void deleteProductVariant(Integer variantId);
    void toggleVariantStatus(Integer variantId, boolean isActive);
    List<ProductVariantResponse> getProductVariants(Integer productId);

    // iinventory
    void updateInventory(Integer variantId, Integer quantity);
    void reserveInventory(Integer variantId, Integer quantity);
    void restoreReserveInventory(Integer variantId, Integer quantity);
    void releaseInventory(Integer variantId, Integer quantity);
    void confirmInventory(Integer variantId, Integer quantity);
    void adjustInventory(Integer variantId, Integer quantity, String reason);

    // public api
    ProductDetailResponse getProductBySlug(String slug);
    ProductDetailResponse getProductById(Integer id);
    Page<ProductListResponse> filterProducts(ProductFilterRequest filter, Pageable pageable);
    Page<ProductListResponse> searchProducts(String keyword, Pageable pageable);
    List<ProductListResponse> getRelatedProducts(Integer productId, int limit);

    // page select
    Page<ProductListResponse> getFeaturedProducts(Pageable pageable);
    Page<ProductListResponse> getNewProducts(Pageable pageable);
    Page<ProductListResponse> getOnSaleProducts(Pageable pageable);
    Page<ProductListResponse> getBestSellers(Pageable pageable);

    // fitler
    FilterOptionsResponse getFilterOptions(Integer categoryId);

    // bulk
    void bulkUpdateStatus(List<Integer> productIds, boolean isActive);
    void bulkDelete(List<Integer> productIds);
}