package com.thacbao.neki.services.impl;

import com.thacbao.neki.enums.OrderStatus;
import com.thacbao.neki.exceptions.common.InvalidException;
import com.thacbao.neki.repositories.jpa.OrderRepository;
import com.thacbao.neki.services.OrderStatisticsService;
import com.thacbao.neki.utils.MessageKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderStatisticsServiceImpl implements OrderStatisticsService {

    private final OrderRepository orderRepository;
    // STATISTICS REPORTS

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getDailyOrderCounts(LocalDate startDate, LocalDate endDate) {
        return orderRepository.getDailyOrderCounts(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getDailyRevenue(LocalDate startDate, LocalDate endDate) {
        return orderRepository.getDailyRevenue(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getTopDistrictsByOrderCount(int limit) {
        return orderRepository.getTopDistrictsByOrderCount(limit);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalRevenue() {
        BigDecimal revenue = orderRepository.getTotalRevenue();
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getRevenueByDateRange(LocalDate startDate, LocalDate endDate) {
        BigDecimal revenue = orderRepository.getRevenueByDateRange(
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59));
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalRevenueByUser(Integer userId) {
        BigDecimal revenue = orderRepository.getTotalRevenueByUser(userId);
        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public long countTotalOrders() {
        return orderRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countOrdersByStatus(String status) {
        OrderStatus orderStatus = parseOrderStatus(status);
        return orderRepository.countByStatus(orderStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public long countOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        return orderRepository.countByCreatedAtBetween(
                startDate.atStartOfDay(),
                endDate.atTime(23, 59, 59));
    }

    @Override
    @Transactional(readOnly = true)
    public long countOrdersByUser(Integer userId) {
        return orderRepository.countByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAverageOrderValue() {
        BigDecimal total = getTotalRevenue();
        long count = countTotalOrders();
        if (count == 0)
            return BigDecimal.ZERO;
        return total.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getMonthlyStatistics(int year) {
        List<Object[]> result = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            LocalDate start = LocalDate.of(year, month, 1);
            LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

            long count = countOrdersByDateRange(start, end);
            BigDecimal revenue = getRevenueByDateRange(start, end);

            result.add(new Object[] { month, count, revenue });
        }
        return result;
    }

    private OrderStatus parseOrderStatus(String status) {
        try {
            return OrderStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidException(MessageKey.ORDER_INVALID_STATUS);
        }
    }
}
