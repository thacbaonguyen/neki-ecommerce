package com.thacbao.neki.repositories.jpa;

import com.thacbao.neki.model.Cart;
import com.thacbao.neki.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    @EntityGraph(attributePaths = {
            "cartItems",
            "cartItems.variant",
            "cartItems.variant.inventory",
            "cartItems.variant.product",
            "cartItems.variant.color",
            "cartItems.variant.size"
    })
    Optional<Cart> findByUserId(Integer userId);

    Optional<Cart> findByUser(User user);
}
