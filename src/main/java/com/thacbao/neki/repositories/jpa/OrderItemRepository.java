package com.thacbao.neki.repositories.jpa;

import com.thacbao.neki.model.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    // Find all items by order id
    List<OrderItem> findByOrderId(Integer orderId);

    // Find items by variant id (for stock checking)
    List<OrderItem> findByVariantId(Integer variantId);

    // Count how many times a variant was ordered
    @Query("SELECT COALESCE(SUM(oi.quantity), 0) FROM OrderItem oi WHERE oi.variant.id = :variantId")
    Integer getTotalQuantitySoldByVariant(@Param("variantId") Integer variantId);

    // Get top selling variants
    @Query("SELECT oi.variant.id, SUM(oi.quantity) as totalQty FROM OrderItem oi " +
            "GROUP BY oi.variant.id ORDER BY totalQty DESC")
    List<Object[]> getTopSellingVariants();

    // Get top selling products (aggregate by product)
    @Query("SELECT oi.variant.product.id, SUM(oi.quantity) as totalQty FROM OrderItem oi " +
            "GROUP BY oi.variant.product.id ORDER BY totalQty DESC")
    List<Object[]> getTopSellingProducts();

    // Delete all items by order
    void deleteByOrderId(Integer orderId);

    Page<OrderItem> findByOrderUserId(Integer userId, Pageable pageable);
}
