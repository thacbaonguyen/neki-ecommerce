package com.thacbao.neki.services.impl;

import com.thacbao.neki.dto.request.product.DiscountRequest;
import com.thacbao.neki.dto.response.DiscountResponse;
import com.thacbao.neki.enums.DiscountType;
import com.thacbao.neki.exceptions.common.InvalidException;
import com.thacbao.neki.exceptions.common.NotFoundException;
import com.thacbao.neki.model.Discount;
import com.thacbao.neki.repositories.jpa.DiscountRepository;
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
    private final DiscountRepository discountRepository;

    @Override
    public void create(DiscountRequest request) {
        if (request.getPercent() != null && request.getReduceAmount() != null) {
            throw new InvalidException("Chỉ được giảm 1 mục (phần trăm hoặc giá)");
        }
        DiscountType discountType = DiscountType.fromValue(request.getDiscountType());

        Discount discount = Discount.builder()
                .name(request.getName())
                .percent(request.getPercent() != null ? request.getPercent() : 0)
                .reduceAmount(request.getReduceAmount() != null ? request.getReduceAmount() : BigDecimal.ZERO)
                .discountType(discountType)
                .description(request.getDescription())
                .isActive(request.isActive())
                .build();
        discountRepository.save(discount);
    }

    @Override
    public void update(Integer id, DiscountRequest request) {
        if (request.getPercent() != null && request.getReduceAmount() != null) {
            throw new InvalidException("Chỉ được giảm 1 mục (phần trăm hoặc giá)");
        }
        DiscountType discountType = DiscountType.fromValue(request.getDiscountType());

        Discount discount = discountRepository.findById(id).orElseThrow(
                () -> new NotFoundException(MessageKey.DISCOUNT_NOT_FOUND)
        );
        discount.setName(request.getName());
        discount.setPercent(request.getPercent() != null ? request.getPercent() : 0);
        discount.setReduceAmount(request.getReduceAmount() != null ? request.getReduceAmount() : BigDecimal.ZERO);
        discount.setDiscountType(discountType);
        discount.setDescription(request.getDescription());
        discount.setActive(request.isActive());
        discountRepository.save(discount);
    }

    @Override
    public void delete(Integer id) {
        Discount discount = discountRepository.findById(id).orElseThrow(
                () -> new NotFoundException(MessageKey.DISCOUNT_NOT_FOUND)
        );
        discountRepository.delete(discount);
    }

    @Override
    public List<DiscountResponse> getAllByType(String discountType) {
        DiscountType type = DiscountType.fromValue(discountType);
        return discountRepository.findByDiscountType(type).stream()
                .map(DiscountResponse::from).collect(Collectors.toList());
    }
}
