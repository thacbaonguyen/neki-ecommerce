package com.thacbao.neki.services;

import com.thacbao.neki.dto.response.ProductListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecommendationService {
    void calculateSimilarities();

    Page<ProductListResponse> getSimilarProducts(Integer productId, Pageable pageable);

    List<ProductListResponse> getRecommendedForYou(Integer userId, int limit);
}
