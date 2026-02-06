package com.thacbao.neki.services;

import com.thacbao.neki.dto.response.WishListResponse;

public interface WishListService {
    void addProductToWishList(int productId);
    void removeProductFromWishList(int productId);

    WishListResponse getAllWishList();
}
