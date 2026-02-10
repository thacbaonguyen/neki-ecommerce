package com.thacbao.neki.repositories.jpa;

import com.thacbao.neki.dto.request.product.OrderFilterRequest;
import com.thacbao.neki.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface OrderRepositoryCustom {

    /**
     * admin request
     *  search by keyword (orderNumber, user email, user fullName),  filter by userId, amount range, date range, district, ward
     */
    Page<Order> filterOrders(OrderFilterRequest filter, Pageable pageable);

    /**
     * find with date range
     */
    Page<Order> findByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable);

    /**
     * get by district
     */
    Page<Order> findByDistrict(String district, Pageable pageable);

    /**
     * count order by day between start and end -> chart
     */
    List<Object[]> getDailyOrderCounts(LocalDate startDate, LocalDate endDate);

    /**
     * solve revenue between start and end
     */
    List<Object[]> getDailyRevenue(LocalDate startDate, LocalDate endDate);

    /**
     * total revenue by user
     */
    BigDecimal getTotalRevenueByUser(Integer userId);

    /**
     * count order by district
     */
    List<Object[]> getTopDistrictsByOrderCount(int limit);
}
