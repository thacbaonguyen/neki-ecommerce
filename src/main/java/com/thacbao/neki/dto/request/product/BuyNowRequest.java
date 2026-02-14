package com.thacbao.neki.dto.request.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuyNowRequest {
    OrderRequest orderRequest;
    OrderItemRequest orderItemRequest;
}
