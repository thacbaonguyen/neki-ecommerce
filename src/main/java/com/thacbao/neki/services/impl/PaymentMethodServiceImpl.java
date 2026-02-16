package com.thacbao.neki.services.impl;

import com.thacbao.neki.dto.request.product.PaymentMethodRequest;
import com.thacbao.neki.dto.response.PaymentMethodResponse;
import com.thacbao.neki.exceptions.common.AlreadyException;
import com.thacbao.neki.exceptions.common.NotFoundException;
import com.thacbao.neki.model.PaymentMethod;
import com.thacbao.neki.repositories.jpa.PaymentMethodRepository;
import com.thacbao.neki.services.PaymentMethodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PaymentMethodServiceImpl implements PaymentMethodService {
    private final PaymentMethodRepository paymentMethodRepository;


    @Override
    public void create(PaymentMethodRequest request) {
        Optional<PaymentMethod> existing = paymentMethodRepository.findByName(request.getName());
        if (existing.isPresent()) {
            throw new AlreadyException("method này đã tồn tại");
        }
        PaymentMethod paymentMethod = PaymentMethod.builder()
                .name(request.getName())
                .description(request.getDescription())
                .isActive(true)
                .build();
        paymentMethodRepository.save(paymentMethod);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentMethodResponse> getAll() {
        return paymentMethodRepository.findAll().stream().map(PaymentMethodResponse::from).collect(Collectors.toList());
    }

    @Override
    public void update(Integer id, boolean status) {
        PaymentMethod method = paymentMethodRepository.findById(id).orElseThrow(
                () -> new NotFoundException("payment method not found")
        );
        method.setIsActive(status);
        paymentMethodRepository.save(method);
    }

    @Override
    public void delete(Integer id) {
        PaymentMethod method = paymentMethodRepository.findById(id).orElseThrow(
                () -> new NotFoundException("payment method not found")
        );
        paymentMethodRepository.delete(method);
    }
}
