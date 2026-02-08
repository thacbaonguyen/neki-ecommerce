package com.thacbao.neki.services.impl;

import com.thacbao.neki.dto.response.WishListResponse;
import com.thacbao.neki.exceptions.common.InvalidException;
import com.thacbao.neki.exceptions.common.NotFoundException;
import com.thacbao.neki.model.Product;
import com.thacbao.neki.model.User;
import com.thacbao.neki.model.Wishlist;
import com.thacbao.neki.repositories.jpa.ProductRepository;
import com.thacbao.neki.repositories.jpa.UserRepository;
import com.thacbao.neki.repositories.jpa.WishListRepository;
import com.thacbao.neki.security.SecurityUtils;
import com.thacbao.neki.services.WishListService;
import com.thacbao.neki.utils.MessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public void addProductToWishList(int productId) {
        log.info("addProductToWishList: {}", productId);
        Integer userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new InvalidException(MessageKey.USER_NOT_LOGIN);
        }
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(MessageKey.USER_NOT_FOUND));

        Wishlist wishlist = wishListRepository.findByUserIdFetchProducts(userId).orElseGet(
                () -> {
                    Wishlist wishlistEntity = Wishlist.builder()
                            .user(user)
                            .build();
                    user.setWishlist(wishlistEntity);
                    return wishListRepository.save(wishlistEntity);
                }
        );

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException(MessageKey.PRODUCT_NOT_FOUND)
        );
        if (!product.getIsActive()) {
            throw new InvalidException(MessageKey.PRODUCT_DOESNT_ACTIVE);
        }

        boolean exists = wishListRepository.existsByUserIdAndProducts_Id(userId, productId);
        if (exists) {
            throw new InvalidException("Sản phẩm đã tồn tại trong wishlist");
        }

        wishlist.getProducts().add(product);
        wishListRepository.save(wishlist);
        log.info("ADDED product to wishlist: {}", product.getName());
    }

    @Override
    @Transactional
    public void removeProductFromWishList(int productId) {
        log.info("Remove product from wishlist: {}", productId);
        Integer userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new InvalidException(MessageKey.USER_NOT_LOGIN);
        }

        Wishlist wishlist = wishListRepository.findByUserIdFetchProducts(userId).orElseThrow(
                () -> new NotFoundException(MessageKey.WISH_LIST_NOT_FOUND)
        );
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException(MessageKey.PRODUCT_NOT_FOUND)
        );

        boolean exists = wishListRepository.existsByUserIdAndProducts_Id(userId, productId);
        if (!exists) {
            throw new InvalidException("Sản phẩm không tồn tại trong wishlist");
        }

        wishlist.getProducts().remove(product);
        log.info("REMOVED product from wishlist: {}", product.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public WishListResponse getAllWishList() {
        Integer userId = SecurityUtils.getCurrentUserId();
        return wishListRepository.findByUserIdFetchProducts(userId)
                .map(WishListResponse::from)
                .orElseGet(
                        WishListResponse::new);
    }
}
