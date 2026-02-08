package com.thacbao.neki.services;

import com.thacbao.neki.dto.request.product.CartRequest;
import com.thacbao.neki.dto.response.CartResponse;


public interface CartService {
    CartResponse addProductToCart(CartRequest request);

    void removeProductFromCart(Integer cartItemId);

    void changeQuantity(Integer cartItemId, Integer quantity);

    void updateProductQuantity(Integer cartItemId, Integer quantity);

    CartResponse getCart();


}
