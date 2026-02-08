package com.thacbao.neki.controllers.user;

import com.thacbao.neki.dto.request.product.CartRequest;
import com.thacbao.neki.dto.response.ApiResponse;
import com.thacbao.neki.dto.response.CartResponse;
import com.thacbao.neki.services.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/add-product")
    public ResponseEntity<ApiResponse<CartResponse>> addProductToCart(@Valid @RequestBody CartRequest cartRequest) {
        CartResponse cartResponse = cartService.addProductToCart(cartRequest);
        return ResponseEntity.ok(
                ApiResponse.<CartResponse>builder()
                        .code(200)
                        .status("success")
                        .data(cartResponse)
                        .build()
        );
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse<Void>> removeItemFromCart(@PathVariable("cartItemId") Integer cartItemId) {
        cartService.removeProductFromCart(cartItemId);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Xóa sản phẩm khỏi giỏ hàng thành công")
                        .build()
        );
    }

    @PutMapping("/item/change/{cartItemId}/{quantity}")
    public ResponseEntity<ApiResponse<Void>> changeQuantityDelta(@PathVariable("cartItemId") Integer cartItemId,
                                                                 @PathVariable("quantity") Integer quantity) {
        cartService.changeQuantity(cartItemId, quantity);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("giảm hoặc tăng sp thành công")
                        .build()
        );
    }

    @PutMapping("/item/update/{cartItemId}/{quantity}")
    public ResponseEntity<ApiResponse<Void>> updateQuantity(@PathVariable("cartItemId") Integer cartItemId,
                                                                 @PathVariable("quantity") Integer quantity) {
        cartService.updateProductQuantity(cartItemId, quantity);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("thay đổi số lượng sp thành công")
                        .build()
        );
    }

    @GetMapping("/items")
    public ResponseEntity<ApiResponse<CartResponse>> getCartItems() {
        CartResponse cartResponse = cartService.getCart();
        return ResponseEntity.ok(
                ApiResponse.<CartResponse>builder()
                        .code(200)
                        .status("success")
                        .data(cartResponse)
                        .build()
        );
    }
}
