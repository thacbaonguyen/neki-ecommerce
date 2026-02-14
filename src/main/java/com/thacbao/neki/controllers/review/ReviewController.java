package com.thacbao.neki.controllers.review;

import com.thacbao.neki.dto.request.product.ReviewRequest;
import com.thacbao.neki.dto.response.ApiResponse;
import com.thacbao.neki.dto.response.ReviewResponse;
import com.thacbao.neki.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(@RequestBody ReviewRequest request) {
        ReviewResponse response = reviewService.create(request);
        return ResponseEntity.ok(
                ApiResponse.<ReviewResponse>builder()
                        .code(200)
                        .status("success")
                        .data(response)
                        .build()
        );
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(@PathVariable("id") Integer id, @RequestBody ReviewRequest request) {
        ReviewResponse response = reviewService.update(id, request);
        return ResponseEntity.ok(
                ApiResponse.<ReviewResponse>builder()
                        .code(200)
                        .status("success")
                        .data(response)
                        .build()
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable("id") Integer id) {
        reviewService.delete(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Xóa thành cồng review")
                        .build()
        );
    }

    @GetMapping("/all-review/product/{productId}")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getAllReview(@PathVariable Integer productId
    , @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ReviewResponse> responses = reviewService.getAllReviewByProduct(productId, pageable);
        return ResponseEntity.ok(
                ApiResponse.<Page<ReviewResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(responses)
                        .build()
        );
    }
}
