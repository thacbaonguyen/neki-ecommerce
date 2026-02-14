package com.thacbao.neki.repositories.jpa;

import com.thacbao.neki.model.Product;
import com.thacbao.neki.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("""
    SELECT 
        COALESCE(AVG(r.rating), 0),
        COUNT(r)
    FROM Review r
    WHERE r.product.id = :productId
""")
    Object[] getRatingStats(@Param("productId") Integer productId);

    Page<Review> findByProduct(Product product, Pageable pageable);

    Page<Review> findByUserIdAndRatingGreaterThanEqual(Integer userId, int i, Pageable pageable);
}
