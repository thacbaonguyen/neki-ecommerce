package com.thacbao.neki.controllers.user;

import com.thacbao.neki.dto.request.product.WishListRequest;
import com.thacbao.neki.dto.response.ApiResponse;
import com.thacbao.neki.dto.response.WishListResponse;
import com.thacbao.neki.services.WishListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishListController {

    private final WishListService wishListService;

    @PostMapping("/add-product")
    public ResponseEntity<ApiResponse<Void>> addProductToWishList(
            @Valid @RequestBody WishListRequest wishListRequest
    ){
        wishListService.addProductToWishList(wishListRequest.getProductId());
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Thêm product vào wishlist thành công")
                        .build()
        );
    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<ApiResponse<Void>> removeProductFromWishList(@PathVariable Integer id) {
        wishListService.removeProductFromWishList(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Xóa sản phẩm khỏi wishlist thành công")
                        .build()
        );
    }

    @GetMapping("/my-wishlist")
    public ResponseEntity<ApiResponse<WishListResponse>> getAllProductFromWishList(){
        WishListResponse wishListResponse = wishListService.getAllWishList();
        return ResponseEntity.ok(
                ApiResponse.<WishListResponse>builder()
                        .code(200)
                        .status("success")
                        .data(wishListResponse)
                        .build()
        );
    }
}
