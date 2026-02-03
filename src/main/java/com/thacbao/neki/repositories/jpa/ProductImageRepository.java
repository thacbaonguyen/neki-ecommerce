package com.thacbao.neki.repositories.jpa;

import com.thacbao.neki.model.Color;
import com.thacbao.neki.model.Product;
import com.thacbao.neki.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {
    List<ProductImage> findByProductOrderByDisplayOrder(Product product);

    List<ProductImage> findByProductAndColor(Product product, Color color);

    @Query("SELECT pi FROM ProductImage pi WHERE pi.product = :product AND pi.isPrimary = true")
    Optional<ProductImage> findPrimaryImageByProduct(@Param("product") Product product);

    void deleteByProduct(Product product);
}