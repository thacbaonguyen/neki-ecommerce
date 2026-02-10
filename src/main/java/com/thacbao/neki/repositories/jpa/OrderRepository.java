package com.thacbao.neki.repositories.jpa;

import com.thacbao.neki.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>, OrderRepositoryCustom {

        // Find order by order number
        Optional<Order> findByOrderNumber(String orderNumber);

        // Check if order number exists
        boolean existsByOrderNumber(String orderNumber);

        // Find all orders by user
        Page<Order> findByUserId(Integer userId, Pageable pageable);

        // Find all orders by user (list)
        List<Order> findByUserIdOrderByCreatedAtDesc(Integer userId);

        // Count orders by user
        long countByUserId(Integer userId);

        // Get total revenue
        @Query("SELECT COALESCE(SUM(o.finalAmount), 0) FROM Order o")
        BigDecimal getTotalRevenue();

        // Get revenue by date range
        @Query("SELECT COALESCE(SUM(o.finalAmount), 0) FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
        BigDecimal getRevenueByDateRange(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        // Count orders by date range
        @Query("SELECT COUNT(o) FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
        long countByDateRange(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        // Get recent orders
        @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
        Page<Order> findRecentOrders(Pageable pageable);

        // Find by user with pagination ordered by createdAt desc
        @Query("SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.createdAt DESC")
        Page<Order> findByUserIdWithPagination(@Param("userId") Integer userId, Pageable pageable);

        // Find by user and status with pagination
        @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.status = :status ORDER BY o.createdAt DESC")
        Page<Order> findByUserIdAndStatus(@Param("userId") Integer userId,
                        @Param("status") com.thacbao.neki.enums.OrderStatus status,
                        Pageable pageable);

        // Count by status
        long countByStatus(com.thacbao.neki.enums.OrderStatus status);

        // Count by date range using LocalDateTime
        long countByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
