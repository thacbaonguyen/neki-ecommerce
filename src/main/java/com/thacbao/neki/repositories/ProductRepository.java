package com.thacbao.neki.repositories;

import com.thacbao.neki.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, ProductRepositoryCustom {

    Optional<Product> findBySlug(String slug);

    boolean existsBySlug(String slug);

    boolean existsByName(String name);

    // Find products by category hierarchy
    @Query("SELECT p FROM Product p WHERE p.subCategory.id IN :subCategoryIds AND p.isActive = true")
    Page<Product> findBySubCategoryIds(@Param("subCategoryIds") List<Integer> subCategoryIds, Pageable pageable);

    // Find featured products
    Page<Product> findByIsFeaturedTrueAndIsActiveTrue(Pageable pageable);

    // Find new products
    Page<Product> findByIsNewTrueAndIsActiveTrue(Pageable pageable);

    // Find on sale products
    @Query("SELECT p FROM Product p WHERE p.salePrice IS NOT NULL AND p.salePrice > 0 AND p.salePrice < p.basePrice AND p.isActive = true")
    Page<Product> findOnSaleProducts(Pageable pageable);

    // Find by price range
    @Query("SELECT p FROM Product p WHERE (p.salePrice IS NULL OR p.salePrice = 0 OR p.salePrice >= :minPrice) " +
            "AND (CASE WHEN p.salePrice IS NOT NULL AND p.salePrice > 0 THEN p.salePrice ELSE p.basePrice END) <= :maxPrice " +
            "AND p.isActive = true")
    Page<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice,
                                   @Param("maxPrice") BigDecimal maxPrice,
                                   Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isActive = true ORDER BY p.totalSold DESC")
    Page<Product> findBestSellers(Pageable pageable);

    // Find popular (by view count)
    @Query("SELECT p FROM Product p WHERE p.isActive = true ORDER BY p.viewCount DESC")
    Page<Product> findPopularProducts(Pageable pageable);

    // Increment view count
    @Modifying
    @Query("UPDATE Product p SET p.viewCount = p.viewCount + 1 WHERE p.id = :productId")
    void incrementViewCount(@Param("productId") Integer productId);

    // Update average rating
    @Modifying
    @Query("UPDATE Product p SET p.averageRating = :rating, p.reviewCount = :count WHERE p.id = :productId")
    void updateRating(@Param("productId") Integer productId,
                      @Param("rating") BigDecimal rating,
                      @Param("count") Integer count);
}

