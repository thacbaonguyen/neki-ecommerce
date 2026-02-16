package com.thacbao.neki.services.impl;

import com.thacbao.neki.dto.request.product.CartRequest;
import com.thacbao.neki.dto.response.CartResponse;
import com.thacbao.neki.exceptions.common.InvalidException;
import com.thacbao.neki.exceptions.common.NotFoundException;
import com.thacbao.neki.model.Cart;
import com.thacbao.neki.model.CartItem;
import com.thacbao.neki.model.ProductVariant;
import com.thacbao.neki.model.User;
import com.thacbao.neki.repositories.jpa.CartItemRepository;
import com.thacbao.neki.repositories.jpa.CartRepository;
import com.thacbao.neki.repositories.jpa.ProductVariantRepository;
import com.thacbao.neki.repositories.jpa.UserRepository;
import com.thacbao.neki.security.SecurityUtils;
import com.thacbao.neki.services.CartService;
import com.thacbao.neki.utils.MessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductVariantRepository productVariantRepository;


    @Override
    public CartResponse addProductToCart(CartRequest request) {
        log.info("add product variant with id {} to cart", request.getVariantId());
        User user = getCurrentUser();
        Cart cart = findOrCreateCart(user);

        ProductVariant variant = productVariantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new NotFoundException(MessageKey.VARIANT_NOT_FOUND));
        int availableQuantity = variant.getInventory().getQuantity()
                - variant.getInventory().getReservedQuantity();

        Optional<CartItem> existingItem = cartItemRepository.findByCartAndVariant(cart, variant);
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + request.getQuantity();
            if (newQuantity > availableQuantity) {
                throw new InvalidException(MessageKey.PRODUCT_NOT_ENOUGH);
            }
            if (newQuantity > 999){
                throw new InvalidException(MessageKey.QUANTITY_SO_BIG);
            }
            item.setQuantity(newQuantity);
        }
        else {
            if (request.getQuantity() > availableQuantity) {
                throw new InvalidException(MessageKey.PRODUCT_NOT_ENOUGH);
            }
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .quantity(request.getQuantity())
                    .variant(variant)
                    .build();
            cart.getCartItems().add(newItem);
        }
        log.info("product {} added to cart", request.getVariantId());

        return CartResponse.from(cartRepository.findByUserId(user.getId()).orElseThrow(
                () -> new NotFoundException("Cart not found")
        ));
    }

    @Override
    public void removeProductFromCart(Integer cartItemId) {
        log.info("remove product variant with id {} from cart", cartItemId);
        User user = getCurrentUser();

        CartItem cartItem = cartItemRepository.findByIdAndCart_User_Id(cartItemId, user.getId()).orElseThrow(
                () -> new NotFoundException(MessageKey.VARIANT_NOT_FOUND)
        );

        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        cart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
        log.info("product {} removed from cart", cartItemId);
    }

    @Override
    public void changeQuantity(Integer cartItemId, Integer quantityDelta) {
        log.info("change quantity by {} for cart item {}", quantityDelta, cartItemId);

        if (quantityDelta != 1 && quantityDelta != -1) {
            throw new InvalidException("Chỉ được tăng hoặc giảm 1 đơn vị");
        }
        User user = getCurrentUser();
        CartItem cartItem = cartItemRepository.findByIdAndCart_User_Id(cartItemId, user.getId()).orElseThrow(
                () -> new NotFoundException(MessageKey.VARIANT_NOT_FOUND)
        );
        int newQuantity = cartItem.getQuantity() + quantityDelta;
        // sso luong <=0 thi tu dong xoa khoi gio hang
        if (newQuantity <= 0) {
            removeProductFromCart(cartItemId);
            return;
        }

        if (newQuantity > 999) {
            throw new InvalidException(MessageKey.QUANTITY_SO_BIG);
        }

        if (quantityDelta > 0) {
            int availableQuantity = cartItem.getVariant().getInventory().getQuantity()
                    - cartItem.getVariant().getInventory().getReservedQuantity();

            if (newQuantity > availableQuantity) {
                throw new InvalidException(MessageKey.PRODUCT_NOT_ENOUGH);
            }
        }
        cartItem.setQuantity(newQuantity);
        log.info("change quantity successfully for cart item with id {} from cart", cartItemId);
    }

    @Override
    public void updateProductQuantity(Integer cartItemId, Integer quantity) {
        log.info("update product quantity for cart item {} from cart", cartItemId);
        if (quantity <= 0 || quantity >= 1000){
            throw new InvalidException(MessageKey.INVALID_INPUT);
        }
        User user = getCurrentUser();
        CartItem cartItem = cartItemRepository.findByIdAndCart_User_Id(cartItemId, user.getId()).orElseThrow(
                () -> new NotFoundException(MessageKey.VARIANT_NOT_FOUND)
        );
        int availableQuantity = cartItem.getVariant().getInventory().getQuantity()
                - cartItem.getVariant().getInventory().getReservedQuantity();

        if (availableQuantity < quantity) {
            throw new InvalidException(MessageKey.PRODUCT_NOT_ENOUGH);
        }
        cartItem.setQuantity(quantity);
        log.info("update product quantity successfully for cart item {} from cart", cartItemId);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart() {
        log.info("get all cart items for user id {}", SecurityUtils.getCurrentUserId());
        Integer userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new InvalidException(MessageKey.USER_NOT_LOGIN);
        }

        log.info("get cart items for user id {} from cart", userId);
        return cartRepository.findByUserId(userId)
                .map(CartResponse::from)
                .orElseGet(CartResponse::new);

    }

    //helper method

    private User getCurrentUser() {
        Integer userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new InvalidException(MessageKey.USER_NOT_LOGIN);
        }

        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(MessageKey.USER_NOT_FOUND)
        );
    }
    // Only used for write
    private Cart findOrCreateCart(User user) {
        return cartRepository.findByUser(user).orElseGet(
                () -> {
                    Cart cartEntity = new Cart();
                    cartEntity.setUser(user);
                    return cartRepository.save(cartEntity);
                }
        );
    }

}
