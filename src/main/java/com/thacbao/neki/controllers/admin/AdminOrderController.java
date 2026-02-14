package com.thacbao.neki.controllers.admin;

import com.thacbao.neki.dto.request.product.BulkUpdateOrderRequest;
import com.thacbao.neki.dto.request.product.ExportOrderRequest;
import com.thacbao.neki.dto.request.product.OrderFilterRequest;
import com.thacbao.neki.dto.request.product.UpdateOrderStatusRequest;
import com.thacbao.neki.dto.response.ApiResponse;
import com.thacbao.neki.dto.response.OrderResponse;
import com.thacbao.neki.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/order")
@RequiredArgsConstructor
public class AdminOrderController {
    private final OrderService orderService;

    @GetMapping()
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getAllOrders(@RequestBody OrderFilterRequest request,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<OrderResponse> orderResponses = orderService.getAllOrders(request, pageable);
        return ResponseEntity.ok(
                ApiResponse.<Page<OrderResponse>>builder()
                        .code(200)
                        .data(orderResponses)
                        .status("success")
                        .build()
        );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Integer orderId) {
        OrderResponse orderResponse = orderService.getOrderByIdAdmin(orderId);
        return ResponseEntity.ok(
                ApiResponse.<OrderResponse>builder()
                        .code(200)
                        .data(orderResponse)
                        .status("success")
                        .build()
        );
    }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(@PathVariable Integer orderId,
                                                                        @RequestBody UpdateOrderStatusRequest request) {
        OrderResponse orderResponse = orderService.updateOrderStatus(orderId, request.getStatus());
        return ResponseEntity.ok(
                ApiResponse.<OrderResponse>builder()
                        .code(200)
                        .data(orderResponse)
                        .status("success")
                        .build()
        );
    }

    @PutMapping("/mark-delivered/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> markOrderAsDelivered(@PathVariable Integer orderId) {
        OrderResponse orderResponse = orderService.markAsDelivered(orderId);
        return ResponseEntity.ok(
                ApiResponse.<OrderResponse>builder()
                        .code(200)
                        .data(orderResponse)
                        .status("success")
                        .build()
        );
    }

    @PutMapping("/bulk-update")
    public ResponseEntity<ApiResponse<Void>> bulkUpdateOrders(@RequestBody BulkUpdateOrderRequest request) {
        orderService.bulkUpdateStatus(request.getOrderIds(), request.getStatus());
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .message("Update status các order thành công")
                        .status("success")
                        .build()
        );
    }

    @GetMapping("/export")
    public ResponseEntity<ApiResponse<byte[]>> exportOrders(@RequestBody ExportOrderRequest request) {
        byte[] bytes = orderService.exportOrders(request.getOrderFilter(), request.getFormat());
        return ResponseEntity.ok(
                ApiResponse.<byte[]>builder()
                        .code(200)
                        .data(bytes)
                        .status("success")
                        .build()
        );
    }
}
