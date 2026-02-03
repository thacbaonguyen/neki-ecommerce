package com.thacbao.neki.dto.request.product;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WishListRequest {
    @NotNull(message = "User không được để trống")
    private Integer userId;

    @NotNull(message = "Product không được để trống")
    private Integer productId;
}
