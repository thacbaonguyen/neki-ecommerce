package com.thacbao.neki.repositories.jpa;

import com.thacbao.neki.model.Color;
import com.thacbao.neki.model.Product;
import com.thacbao.neki.model.ProductVariant;
import com.thacbao.neki.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {
    List<ProductVariant> findByProduct(Product product);

    @Query("SELECT pv FROM ProductVariant pv " +
            "LEFT JOIN FETCH pv.inventory " +
            "WHERE pv.product = :product AND pv.isActive = true")
    List<ProductVariant> findByProductWithInventory(@Param("product") Product product);

    Optional<ProductVariant> findByProductAndColorAndSize(Product product, Color color, Size size);

    boolean existsByProductAndColorAndSize(Product product, Color color, Size size);

    @Query("SELECT pv FROM ProductVariant pv WHERE pv.product.id = :productId AND pv.isActive = true")
    List<ProductVariant> findActiveVariantsByProductId(@Param("productId") Integer productId);
}
