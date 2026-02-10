package com.thacbao.neki.repositories.jpa;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thacbao.neki.dto.request.product.OrderFilterRequest;
import com.thacbao.neki.model.Order;
import com.thacbao.neki.model.QOrder;
import com.thacbao.neki.model.QOrderItem;
import com.thacbao.neki.model.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private final QOrder order = QOrder.order;
    private final QUser user = QUser.user;
    private final QOrderItem orderItem = QOrderItem.orderItem;

    @Override
    public Page<Order> filterOrders(OrderFilterRequest filter, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        // keyword (orderNumber, user email, user fullName)
        if (filter.getKeyword() != null && !filter.getKeyword().trim().isEmpty()) {
            String keyword = "%" + filter.getKeyword().trim().toLowerCase() + "%";
            builder.and(
                    order.orderNumber.toLowerCase().like(keyword)
                            .or(order.user.email.toLowerCase().like(keyword))
                            .or(order.user.fullName.toLowerCase().like(keyword)));
        }

        // filter by userId
        if (filter.getUserId() != null) {
            builder.and(order.user.id.eq(filter.getUserId()));
        }

        // filter by amount range
        if (filter.getMinAmount() != null) {
            builder.and(order.finalAmount.goe(filter.getMinAmount()));
        }
        if (filter.getMaxAmount() != null) {
            builder.and(order.finalAmount.loe(filter.getMaxAmount()));
        }

        // filter by date range
        if (filter.getStartDate() != null) {
            LocalDateTime startDateTime = filter.getStartDate().atStartOfDay();
            builder.and(order.createdAt.goe(startDateTime));
        }
        if (filter.getEndDate() != null) {
            LocalDateTime endDateTime = filter.getEndDate().atTime(LocalTime.MAX);
            builder.and(order.createdAt.loe(endDateTime));
        }

        // filter by district
        if (filter.getDistrict() != null && !filter.getDistrict().trim().isEmpty()) {
            builder.and(order.district.equalsIgnoreCase(filter.getDistrict().trim()));
        }

        // filter by ward
        if (filter.getWard() != null && !filter.getWard().trim().isEmpty()) {
            builder.and(order.ward.equalsIgnoreCase(filter.getWard().trim()));
        }

        // build query
        JPAQuery<Order> query = queryFactory
                .selectFrom(order)
                .leftJoin(order.user, user).fetchJoin()
                .where(builder);

        query.orderBy(getOrderSpecifiers(pageable.getSort()));

        long total = queryFactory
                .selectFrom(order)
                .where(builder)
                .fetchCount();

        List<Order> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Order> findByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        JPAQuery<Order> query = queryFactory
                .selectFrom(order)
                .leftJoin(order.user, user).fetchJoin()
                .where(order.createdAt.between(startDateTime, endDateTime))
                .orderBy(getOrderSpecifiers(pageable.getSort()));

        long total = queryFactory
                .selectFrom(order)
                .where(order.createdAt.between(startDateTime, endDateTime))
                .fetchCount();

        List<Order> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Order> findByDistrict(String district, Pageable pageable) {
        JPAQuery<Order> query = queryFactory
                .selectFrom(order)
                .leftJoin(order.user, user).fetchJoin()
                .where(order.district.equalsIgnoreCase(district))
                .orderBy(getOrderSpecifiers(pageable.getSort()));

        long total = queryFactory
                .selectFrom(order)
                .where(order.district.equalsIgnoreCase(district))
                .fetchCount();

        List<Order> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(content, pageable, total);
    }

    /**
     *
     * @param startDate
     * @param endDate
     * @return {
     *     "date": "2026-02-01",
     *     "orderCount": 15
     *   }
     */
    @Override
    public List<Object[]> getDailyOrderCounts(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Tuple> results = queryFactory
                .select(order.createdAt.year(), order.createdAt.month(), order.createdAt.dayOfMonth(), order.count())
                .from(order)
                .where(order.createdAt.between(startDateTime, endDateTime))
                .groupBy(order.createdAt.year(), order.createdAt.month(), order.createdAt.dayOfMonth())
                .orderBy(order.createdAt.year().asc(), order.createdAt.month().asc(),
                        order.createdAt.dayOfMonth().asc())
                .fetch();

        List<Object[]> result = new ArrayList<>();
        for (Tuple tuple : results) {
            LocalDate date = LocalDate.of(tuple.get(0, Integer.class), tuple.get(1, Integer.class),
                    tuple.get(2, Integer.class));
            result.add(new Object[] { date, tuple.get(3, Long.class) });
        }
        return result;
    }

    /**
     *
     * @param startDate
     * @param endDate
     * @return {
     *     "date": "2026-02-01",
     *     "revenue": 4500000.00
     *   },
     */
    @Override
    public List<Object[]> getDailyRevenue(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Tuple> results = queryFactory
                .select(order.createdAt.year(), order.createdAt.month(), order.createdAt.dayOfMonth(),
                        order.finalAmount.sum())
                .from(order)
                .where(order.createdAt.between(startDateTime, endDateTime))
                .groupBy(order.createdAt.year(), order.createdAt.month(), order.createdAt.dayOfMonth())
                .orderBy(order.createdAt.year().asc(), order.createdAt.month().asc(),
                        order.createdAt.dayOfMonth().asc())
                .fetch();

        List<Object[]> result = new ArrayList<>();
        for (Tuple tuple : results) {
            LocalDate date = LocalDate.of(tuple.get(0, Integer.class), tuple.get(1, Integer.class),
                    tuple.get(2, Integer.class));
            result.add(new Object[] { date, tuple.get(3, BigDecimal.class) });
        }
        return result;
    }

    @Override
    public BigDecimal getTotalRevenueByUser(Integer userId) {
        BigDecimal revenue = queryFactory
                .select(order.finalAmount.sum())
                .from(order)
                .where(order.user.id.eq(userId))
                .fetchOne();

        return revenue != null ? revenue : BigDecimal.ZERO;
    }

    /**
     * @param limit
     * @return {
     *     "district": "Quận HĐ ",
     *     "orderCount": 156
     *   },
     *   {
     *     "district": "Quận TX",
     *     "orderCount": 124
     *   },
     */
    @Override
    public List<Object[]> getTopDistrictsByOrderCount(int limit) {
        List<Tuple> results = queryFactory
                .select(order.district, order.count())
                .from(order)
                .groupBy(order.district)
                .orderBy(order.count().desc())
                .limit(limit)
                .fetch();

        List<Object[]> result = new ArrayList<>();
        for (Tuple tuple : results) {
            result.add(new Object[] { tuple.get(0, String.class), tuple.get(1, Long.class) });
        }
        return result;
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();

        for (Sort.Order sortOrder : sort) {
            com.querydsl.core.types.Order direction = sortOrder.isAscending()
                    ? com.querydsl.core.types.Order.ASC
                    : com.querydsl.core.types.Order.DESC;

            switch (sortOrder.getProperty()) {
                case "orderNumber":
                    orders.add(new OrderSpecifier<>(direction, order.orderNumber));
                    break;
                case "totalAmount":
                    orders.add(new OrderSpecifier<>(direction, order.totalAmount));
                    break;
                case "finalAmount":
                    orders.add(new OrderSpecifier<>(direction, order.finalAmount));
                    break;
                case "district":
                    orders.add(new OrderSpecifier<>(direction, order.district));
                    break;
                case "createdAt":
                    orders.add(new OrderSpecifier<>(direction, order.createdAt));
                    break;
                case "updatedAt":
                    orders.add(new OrderSpecifier<>(direction, order.updatedAt));
                    break;
                default:
                    orders.add(new OrderSpecifier<>(direction, order.createdAt));
                    break;
            }
        }

        if (orders.isEmpty()) {
            orders.add(new OrderSpecifier<>(com.querydsl.core.types.Order.DESC, order.createdAt));
        }

        return orders.toArray(new OrderSpecifier[0]);
    }
}
