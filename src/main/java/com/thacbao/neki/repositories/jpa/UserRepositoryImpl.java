package com.thacbao.neki.repositories.jpa;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thacbao.neki.model.QRole;
import com.thacbao.neki.model.QUser;
import com.thacbao.neki.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final QUser user = QUser.user;
    private final QRole role = QRole.role;

    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<OrderSpecifier<?>>();
        for (Sort.Order order : sort) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            switch (order.getProperty()){
                case "createdAt":
                    orderSpecifiers.add(new OrderSpecifier<>(direction, user.createdAt));
                    break;
                case "fullName":
                    orderSpecifiers.add(new OrderSpecifier<>(direction, user.fullName));
                    break;
                case "email":
                    orderSpecifiers.add(new OrderSpecifier<>(direction, user.email));
                    break;
                default:
                    orderSpecifiers.add(new OrderSpecifier<>(direction, user.createdAt));
                    break;
            }
        }
        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }


    @Override
    public Page<User> findAllActiveUsers(Pageable pageable) {
        JPAQuery<User> query = queryFactory.selectFrom(user)
                .leftJoin(user.roles, role)
                .fetchJoin()
                .where(user.isActive.isTrue(),
                        user.emailVerified.isTrue());

        query.orderBy(getOrderSpecifiers(pageable.getSort()));

        List<User> content = query.offset(pageable.getOffset())
        .limit(pageable.getPageSize()).fetch();

        long total = queryFactory.select(user.count())
                .from(user)
                .where(user.isActive.isTrue(), user.emailVerified.isTrue())
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<User> findAllBlockedUsers(Pageable pageable) {
        JPAQuery<User> query = queryFactory.selectFrom(user)
                .leftJoin(user.roles, role).fetchJoin()
                .where(user.isActive.isFalse());
        query.orderBy(getOrderSpecifiers(pageable.getSort()));

        List<User> content = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize()).fetch();

        long total = queryFactory.select(user.count()).from(user)
                .where(user.isActive.isFalse()).fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<User> findUsersByRole(String roleName, Pageable pageable) {
        JPAQuery<User> query = queryFactory.selectFrom(user)
                .leftJoin(user.roles, role).fetchJoin()
                .where(role.name.eq(roleName),
                        user.isActive.isTrue());

        query.orderBy(getOrderSpecifiers(pageable.getSort()));

        List<User> content = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize()).fetch();

        long total = queryFactory.select(user.count()).from(user)
                .join(user.roles, role)
                .where(role.name.eq(roleName) ,user.isActive.isTrue()).fetchOne();

        return new PageImpl<>(content, pageable, total);

    }

    @Override
    public long countActiveVerifiedUsers() {
        return queryFactory
                .selectFrom(user)
                .where(
                        user.isActive.isTrue(),
                        user.emailVerified.isTrue()
                )
                .fetchCount();
    }
}
