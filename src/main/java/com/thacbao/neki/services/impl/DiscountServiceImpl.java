package com.thacbao.neki.services.impl;

import com.thacbao.neki.dto.request.product.DiscountRequest;
import com.thacbao.neki.dto.response.DiscountResponse;
import com.thacbao.neki.enums.DiscountType;
import com.thacbao.neki.exceptions.common.InvalidException;
import com.thacbao.neki.exceptions.common.NotFoundException;
import com.thacbao.neki.model.Discount;
import com.thacbao.neki.model.DiscountUsage;
import com.thacbao.neki.model.Order;
import com.thacbao.neki.model.User;
import com.thacbao.neki.repositories.jpa.DiscountRepository;
import com.thacbao.neki.repositories.jpa.DiscountUsageRepository;
import com.thacbao.neki.services.DiscountService;
import com.thacbao.neki.utils.MessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DiscountServiceImpl implements DiscountService {
    private final DiscountUsageRepository discountUsageRepository;
    private final DiscountRepository discountRepository;

    @Override
    public void create(DiscountRequest request) {
        validateRequest(request);
        DiscountType discountType = DiscountType.fromValue(request.getDiscountType());

        Discount discount = Discount.builder()
                .name(request.getName())
                .percent(request.getPercent())
                .reduceAmount(request.getReduceAmount())
                .discountType(discountType)
                .description(request.getDescription())
                .isActive(request.isActive())
                .usageLimit(request.getUsageLimit())
                .userUsageLimit(request.getUserUsageLimit())
                .minOrderAmount(request.getMinOrderAmount())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .usedCount(0)
                .build();
        discountRepository.save(discount);
    }

    @Override
    public void update(Integer id, DiscountRequest request) {
        validateRequest(request);
        Discount discount = discountRepository.findById(id).orElseThrow(
                () -> new NotFoundException(MessageKey.DISCOUNT_NOT_FOUND));

        discount.setName(request.getName());
        discount.setPercent(request.getPercent());
        discount.setReduceAmount(request.getReduceAmount());
        discount.setDiscountType(DiscountType.fromValue(request.getDiscountType()));
        discount.setDescription(request.getDescription());
        discount.setActive(request.isActive());
        discount.setUsageLimit(request.getUsageLimit());
        discount.setUserUsageLimit(request.getUserUsageLimit());
        discount.setMinOrderAmount(request.getMinOrderAmount());
        discount.setStartDate(request.getStartDate());
        discount.setEndDate(request.getEndDate());

        discountRepository.save(discount);
    }

    private void validateRequest(DiscountRequest request) {
        if (request.getPercent() != null && request.getReduceAmount() != null) {
            throw new InvalidException("Chỉ được giảm 1 mục (phần trăm hoặc giá)");
        }
    }

    @Override
    public void delete(Integer id) {
        Discount discount = discountRepository.findById(id).orElseThrow(
                () -> new NotFoundException(MessageKey.DISCOUNT_NOT_FOUND));
        discountRepository.delete(discount);
    }

    @Override
    public List<DiscountResponse> getAllByType(String discountType) {
        DiscountType type = DiscountType.fromValue(discountType);
        return discountRepository.findByDiscountType(type).stream()
                .map(DiscountResponse::from).collect(Collectors.toList());
    }

    @Override
    public Discount validateAndGetDiscount(String code, User user, BigDecimal orderAmount) {
        Discount discount = discountRepository.findByName(code)
                .orElseThrow(() -> new NotFoundException(MessageKey.DISCOUNT_NOT_FOUND));

        if (!discount.isActive()) {
            throw new InvalidException(MessageKey.DISCOUNT_INACTIVE);
        }

        // Check dates
        java.time.LocalDate now = java.time.LocalDate.now();
        if (discount.getStartDate() != null && now.isBefore(discount.getStartDate())) {
            throw new InvalidException(MessageKey.DISCOUNT_NOT_STARTED);
        }
        if (discount.getEndDate() != null && now.isAfter(discount.getEndDate())) {
            throw new InvalidException(MessageKey.DISCOUNT_EXPIRED);
        }

        // Check min order
        if (discount.getMinOrderAmount() != null && orderAmount.compareTo(discount.getMinOrderAmount()) < 0) {
            throw new InvalidException(MessageKey.DISCOUNT_MIN_ORDER_NOT_MET);
        }

        // Check total usage
        if (discount.getUsageLimit() != null && discount.getUsedCount() >= discount.getUsageLimit()) {
            throw new InvalidException(MessageKey.DISCOUNT_USAGE_LIMIT_REACHED);
        }

        // Check user usage
        if (discount.getUserUsageLimit() != null) {
            long usedCount = discountUsageRepository.countByUserAndDiscount(user, discount);
            if (usedCount >= discount.getUserUsageLimit()) {
                throw new InvalidException(MessageKey.DISCOUNT_USER_USAGE_LIMIT_REACHED);
            }
        }

        return discount;
    }

    @Override
    public void recordUsage(Discount discount, User user, Order order) {
        discount.setUsedCount(discount.getUsedCount() + 1);
        discountRepository.save(discount);

        DiscountUsage usage = DiscountUsage.builder()
                .discount(discount)
                .user(user)
                .order(order)
                .build();
        discountUsageRepository.save(usage);
    }
}
