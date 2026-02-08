package com.thacbao.neki.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.thacbao.neki.model.Cart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartResponse {
    private Integer id;

    private UserResponseDTO user;
    private Set<CartItemResponse> cartItems;

    public static CartResponse from(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .user(UserResponseDTO.from(cart.getUser()))
                .cartItems(cart.getCartItems().stream().map(CartItemResponse::from).collect(Collectors.toSet()))
                .build();
    }
}
