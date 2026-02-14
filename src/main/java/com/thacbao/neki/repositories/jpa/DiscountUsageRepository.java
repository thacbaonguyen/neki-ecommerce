package com.thacbao.neki.repositories.jpa;

import com.thacbao.neki.model.Discount;
import com.thacbao.neki.model.DiscountUsage;
import com.thacbao.neki.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountUsageRepository extends JpaRepository<DiscountUsage, Integer> {
    long countByUserAndDiscount(User user, Discount discount);
}
