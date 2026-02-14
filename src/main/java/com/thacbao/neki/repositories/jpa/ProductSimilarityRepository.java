package com.thacbao.neki.repositories.jpa;

import com.thacbao.neki.model.Product;
import com.thacbao.neki.model.ProductSimilarity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSimilarityRepository extends JpaRepository<ProductSimilarity, Integer> {

    @Query("SELECT ps FROM ProductSimilarity ps WHERE ps.product1.id = :productId ORDER BY ps.score DESC")
    List<ProductSimilarity> findSimilarProducts(Integer productId, Pageable pageable);

    @Modifying
    @Query("DELETE FROM ProductSimilarity")
    void deleteAllInBatchCustom();
}
