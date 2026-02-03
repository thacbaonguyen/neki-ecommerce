package com.thacbao.neki.repositories.jpa;

import com.thacbao.neki.dto.request.product.ProductFilterRequest;
import com.thacbao.neki.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepositoryCustom {

    /**
     * Advanced product filtering with multiple criteria
     */
    Page<Product> filterProducts(ProductFilterRequest filter, Pageable pageable);

    /**
     * Get products by collection
     */
    Page<Product> findByCollectionId(Integer collectionId, Pageable pageable);

    /**
     * Get products by topic
     */
    Page<Product> findByTopicId(Integer topicId, Pageable pageable);

    /**
     * Get products by brand
     */
    Page<Product> findByBrandId(Integer brandId, Pageable pageable);

    /**
     * Get related products (same subcategory, different product)
     */
    List<Product> findRelatedProducts(Integer productId, Integer subCategoryId, int limit);

    /**
     * Get products with available stock
     */
    Page<Product> findProductsWithStock(Pageable pageable);

    /**
     * Get price range for a category
     */
    BigDecimal[] getPriceRangeByCategory(Integer categoryId);

    /**
     * Get available colors for filtered products
     */
    List<String> getAvailableColors(ProductFilterRequest filter);

    /**
     * Get available sizes for filtered products
     */
    List<String> getAvailableSizes(ProductFilterRequest filter);
}