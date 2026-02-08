package com.thacbao.neki.repositories.jpa;

import com.thacbao.neki.model.Cart;
import com.thacbao.neki.model.CartItem;
import com.thacbao.neki.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    Optional<CartItem> findByIdAndCart_User_Id(Integer cartItemId, Integer userId);

    Optional<CartItem> findByCartAndVariant(Cart cart, ProductVariant productVariant);

}
