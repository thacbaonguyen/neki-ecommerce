package com.thacbao.neki.dto.request.product;

import lombok.Data;

@Data
public class ExportOrderRequest {
    private OrderFilterRequest orderFilter;
    private String format;
}
