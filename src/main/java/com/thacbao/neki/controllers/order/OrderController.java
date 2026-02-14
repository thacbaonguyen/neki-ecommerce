package com.thacbao.neki.controllers.order;

import com.thacbao.neki.dto.request.product.*;
import com.thacbao.neki.dto.response.ApiResponse;
import com.thacbao.neki.dto.response.OrderResponse;
import com.thacbao.neki.dto.response.OrderSummaryResponse;
import com.thacbao.neki.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping(path = "/create")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrderFromCart(@RequestBody OrderRequest request) {
        OrderResponse orderResponse = orderService.createOrderFromCart(request);
        return ResponseEntity.ok(
                ApiResponse.<OrderResponse>builder()
                        .code(200)
                        .status("success")
                        .data(orderResponse)
                        .build()
        );
    }

    @PostMapping(path = "/create-selected")
    public ResponseEntity<ApiResponse<OrderResponse>> createSelectedOrderFromCart(
            @RequestBody CreateSelectedOrderRequest request) {
        OrderResponse orderResponse = orderService.createOrderFromSelectedItems(request.getOrderRequest(), request.getOrderItemRequests());
        return ResponseEntity.ok(
                ApiResponse.<OrderResponse>builder()
                        .code(200)
                        .status("success")
                        .data(orderResponse)
                        .build()
        );
    }

    @PostMapping(path = "/buy-now")
    public ResponseEntity<ApiResponse<OrderResponse>> buyNow(@RequestBody BuyNowRequest request) {
        OrderResponse orderResponse = orderService.buyNow(request.getOrderRequest(), request.getOrderItemRequest());
        return ResponseEntity.ok(
                ApiResponse.<OrderResponse>builder()
                        .code(200)
                        .status("success")
                        .data(orderResponse)
                        .build()
        );
    }

    // get

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable("id") Integer id) {
        OrderResponse orderResponse = orderService.getOrderById(id);
        return ResponseEntity.ok(
                ApiResponse.<OrderResponse>builder()
                        .code(200)
                        .status("success")
                        .data(orderResponse)
                        .build()
        );
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderByOrderNumber(@PathVariable("orderNumber") String orderNumber) {
        OrderResponse orderResponse = orderService.getOrderByOrderNumber(orderNumber);
        return ResponseEntity.ok(
                ApiResponse.<OrderResponse>builder()
                        .code(200)
                        .status("success")
                        .data(orderResponse)
                        .build()
        );
    }

    @GetMapping("/my-order")
    public ResponseEntity<ApiResponse<Page<OrderSummaryResponse>>> getMyOrder(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<OrderSummaryResponse> orderResponses = orderService.getMyOrders(pageable);

        return ResponseEntity.ok(
                ApiResponse.<Page<OrderSummaryResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(orderResponses)
                        .build()
        );
    }

    @GetMapping("/my-order/{status}")
    public ResponseEntity<ApiResponse<Page<OrderSummaryResponse>>> getMyOrderByStatus(@PathVariable("status") String status,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<OrderSummaryResponse> orderResponses = orderService.getMyOrdersByStatus(status, pageable);

        return ResponseEntity.ok(
                ApiResponse.<Page<OrderSummaryResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(orderResponses)
                        .build()
        );
    }

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable("orderId") Integer orderId, @RequestBody CancelOrderRequest request) {
        orderService.cancelOrder(orderId, request.getReason());
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Hủy đơn hàng thành công")
                        .build()
        );
    }

    @PostMapping("/re-order/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> reOrder(@PathVariable("orderId") Integer orderId) {
        OrderResponse orderResponse = orderService.reOrder(orderId);
        return ResponseEntity.ok(
                ApiResponse.<OrderResponse>builder()
                        .code(200)
                        .status("success")
                        .data(orderResponse)
                        .build()
        );
    }

    @GetMapping("/tracking/{orderNumber}")
    public ResponseEntity<ApiResponse<OrderResponse>> trackingOrder(@PathVariable("orderNumber") String orderNumber,
                                                                    @RequestParam String email) {
        OrderResponse orderResponse = orderService.trackOrder(orderNumber, email);
        return ResponseEntity.ok(
                ApiResponse.<OrderResponse>builder()
                        .code(200)
                        .status("success")
                        .data(orderResponse)
                        .build()
        );
    }

    @GetMapping("/order-timeline/{orderId}")
    public ResponseEntity<ApiResponse<List<Object>>> getOrderTimeline(@PathVariable("orderId") Integer orderId) {
        List<Object> timeline = orderService.getOrderTimeline(orderId);
        return ResponseEntity.ok(
                ApiResponse.<List<Object>>builder()
                        .code(200)
                        .status("success")
                        .data(timeline)
                        .build()
        );
    }

}