package com.thacbao.neki.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.thacbao.neki.model.Product;
import com.thacbao.neki.model.Wishlist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WishListResponse {
    private Integer id;
    private UserResponseDTO user;
    private LocalDateTime createdAt;
    private List<ProductListResponse> product;

    public WishListResponse from(Wishlist wishlist) {
        return WishListResponse.builder()
                .id(wishlist.getId())
                .user(UserResponseDTO.from(wishlist.getUser()))
                .createdAt(wishlist.getCreatedAt())
                .product(toProductList(wishlist.getProducts()))
                .build();
    }

    private List<ProductListResponse> toProductList(Set<Product> products) {
        return products.stream().map(ProductListResponse::from).collect(Collectors.toList());
    }
}
