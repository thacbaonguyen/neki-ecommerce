package com.thacbao.neki.dto.request.product;

import lombok.Data;

import java.util.List;

@Data
public class BulkUpdateOrderRequest {
    private List<Integer> orderIds;
    private String status;
}
