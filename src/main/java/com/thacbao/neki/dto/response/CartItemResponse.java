package com.thacbao.neki.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.thacbao.neki.model.CartItem;
import com.thacbao.neki.model.Product;
import com.thacbao.neki.model.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemResponse {
    private Integer id;
    private ProductVariantResponse variant;
    private Integer quantity;
    private String productName;
    private String productSlug;
    private String productImage;
    private String subCategoryName;

    public static CartItemResponse from(CartItem cartItem) {
        Product product = cartItem.getVariant().getProduct();
        String primaryImage = product.getImages().stream()
                .filter(ProductImage::getIsPrimary)
                .findFirst()
                .map(ProductImage::getImageUrl)
                .orElse(product.getImages().stream()
                        .findFirst()
                        .map(ProductImage::getImageUrl)
                        .orElse(null));

        return CartItemResponse.builder()
                .id(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .variant(ProductVariantResponse.from(cartItem.getVariant(), product.getCurrentPrice()))
                .productName(product.getName())
                .productSlug(product.getSlug())
                .productImage(primaryImage)
                .subCategoryName(product.getSubCategory().getName())
                .build();
    }
}
