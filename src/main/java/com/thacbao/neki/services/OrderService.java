package com.thacbao.neki.services;

import com.thacbao.neki.dto.request.product.OrderFilterRequest;
import com.thacbao.neki.dto.request.product.OrderItemRequest;
import com.thacbao.neki.dto.request.product.OrderRequest;
import com.thacbao.neki.dto.response.OrderResponse;
import com.thacbao.neki.dto.response.OrderSummaryResponse;
import com.thacbao.neki.enums.DiscountType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderService {

    // user method
    /**
     * check out all cart
     */
    OrderResponse createOrderFromCart(OrderRequest request);

    /**
     * check out 1 số sp
     */
    OrderResponse createOrderFromSelectedItems(OrderRequest request, List<OrderItemRequest> items);

    /**
     * Buy now - not from cart
     */
    OrderResponse buyNow(OrderRequest request, OrderItemRequest item);

    /**
     *  get order detail by id ( only self)
     */
    OrderResponse getOrderById(Integer orderId);

    /**
     * get order by order number
     */
    OrderResponse getOrderByOrderNumber(String orderNumber);

    /**
     * get all order of current user
     */
    Page<OrderSummaryResponse> getMyOrders(Pageable pageable);

    /**
     * Get orders by status for current user
     */
    Page<OrderSummaryResponse> getMyOrdersByStatus(String status, Pageable pageable);

    /**
     * Cancel order (PENDING)
     */
    void cancelOrder(Integer orderId, String reason);

    /**
     * tao lai order tu order cu~
     */
    OrderResponse reOrder(Integer orderId);

    // ADMIN

    /**
     * get all orders with filter
     */
    Page<OrderResponse> getAllOrders(OrderFilterRequest filter, Pageable pageable);

    /**
     * get order detail by id (admin xem dc all order)
     */
    OrderResponse getOrderByIdAdmin(Integer orderId);

    /**
     * update order status
     *  PENDING -> CONFIRMED -> PROCESSING -> SHIPPING -> DELIVERED
     */
    OrderResponse updateOrderStatus(Integer orderId, String status);

    /**
     * Mark order as delivered
     */
    OrderResponse markAsDelivered(Integer orderId);

    /**
     * Process refund for order
     */
    void processRefund(Integer orderId, BigDecimal refundAmount, String reason);

    /**
     * Bulk update order status
     */
    void bulkUpdateStatus(List<Integer> orderIds, String status);

    /**
     * Export orders to Excel/CSV
     */
    byte[] exportOrders(OrderFilterRequest filter, String format);

    //  ORDER TRACKING

    /**
     * Track order by order number (public - không cần đăng nhập)
     */
    OrderResponse trackOrder(String orderNumber, String email);

    /**
     * Get order timeline/history
     */
    List<Object> getOrderTimeline(Integer orderId);

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

    // VALIDATION & UTILITY

    /**
     * Validate order before create (check stock, validate variants, etc.)
     */
    void validateOrder(List<OrderItemRequest> items);

    /**
     * Check if order can be cancelled
     */
    boolean canCancelOrder(Integer orderId);

    /**
     * Generate unique order number
     */
    String generateOrderNumber();

    //  DISCOUNT & PROMOTION

    /**
     * Apply discount code to order
     */
    Map<DiscountType, BigDecimal> applyDiscountCode(String discountCode, BigDecimal orderAmount, BigDecimal shippingFee);


    /**
     * Calculate shipping fee based on address
     */
    BigDecimal calculateShippingFee(String district, String ward, BigDecimal orderAmount);
}
