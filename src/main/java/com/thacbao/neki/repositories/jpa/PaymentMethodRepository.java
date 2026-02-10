package com.thacbao.neki.repositories.jpa;

import com.thacbao.neki.model.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
    Optional<PaymentMethod> findByName(String name);
}
