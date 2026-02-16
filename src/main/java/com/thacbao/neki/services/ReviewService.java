package com.thacbao.neki.services;

import com.thacbao.neki.dto.request.product.ReviewRequest;
import com.thacbao.neki.dto.response.ReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    ReviewResponse create(ReviewRequest reviewRequest);

    ReviewResponse update(Integer id, ReviewRequest reviewRequest);

    void delete(Integer id);

    Page<ReviewResponse> getAllReviewByProduct(Integer productId, Pageable pageable);
}
