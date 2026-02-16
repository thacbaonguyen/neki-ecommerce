package com.thacbao.neki.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface OrderStatisticsService {
    // statistic and report

    /**
     * Get daily order counts for date range
     *
     * @return List of {date, orderCount}
     */
    List<Object[]> getDailyOrderCounts(LocalDate startDate, LocalDate endDate);

    /**
     * Get daily revenue for date range
     *
     * @return List of {date, revenue}
     */
    List<Object[]> getDailyRevenue(LocalDate startDate, LocalDate endDate);

    /**
     * Get top districts by order count
     *
     * @return List of {district, orderCount}
     */
    List<Object[]> getTopDistrictsByOrderCount(int limit);

    /**
     *  total revenue
     */
    BigDecimal getTotalRevenue();

    /**
     *  revenue by date range
     */
    BigDecimal getRevenueByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     *  total revenue by user
     */
    BigDecimal getTotalRevenueByUser(Integer userId);

    /**
     *  total orders
     */
    long countTotalOrders();

    /**
     * count orders by status
     */
    long countOrdersByStatus(String status);

    /**
     * count orders by date range
     */
    long countOrdersByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     *  count by user
     */
    long countOrdersByUser(Integer userId);

    /**
     * Get average order value
     */
    BigDecimal getAverageOrderValue();

    /**
     * Get monthly order statistics
     *
     * @return List of {month, orderCount, revenue}
     */
    List<Object[]> getMonthlyStatistics(int year);
}
