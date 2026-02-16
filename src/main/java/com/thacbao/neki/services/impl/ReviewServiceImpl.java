package com.thacbao.neki.services.impl;

import com.thacbao.neki.dto.request.product.ReviewRequest;
import com.thacbao.neki.dto.response.ReviewResponse;
import com.thacbao.neki.exceptions.common.InvalidException;
import com.thacbao.neki.exceptions.common.NotFoundException;
import com.thacbao.neki.exceptions.user.PermissionException;
import com.thacbao.neki.model.Order;
import com.thacbao.neki.model.Product;
import com.thacbao.neki.model.Review;
import com.thacbao.neki.model.User;
import com.thacbao.neki.repositories.jpa.OrderRepository;
import com.thacbao.neki.repositories.jpa.ProductRepository;
import com.thacbao.neki.repositories.jpa.ReviewRepository;
import com.thacbao.neki.repositories.jpa.UserRepository;
import com.thacbao.neki.security.SecurityUtils;
import com.thacbao.neki.services.ReviewService;
import com.thacbao.neki.utils.MessageKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    @Override
    public ReviewResponse create(ReviewRequest reviewRequest) {
        log.info("create review :{}", reviewRequest.getProductId());
        User user = getCurrentUser();

        Product product = productRepository.findById(reviewRequest.getProductId()).orElseThrow(
                () -> new NotFoundException(MessageKey.PRODUCT_NOT_FOUND)
        );
        if (!product.getIsActive()){
            throw new InvalidException(MessageKey.PRODUCT_DOESNT_ACTIVE);
        }

        Optional<Order> order = orderRepository.findOrdersUserCanReview(user.getId(), reviewRequest.getProductId(), PageRequest.of(0, 1)).stream()
                .findFirst();;
        if (order.isEmpty()) {
            throw new PermissionException(MessageKey.USER_CANNOT_REVIEW);
        }

        Review review = Review.builder()
                .user(user)
                .product(product)
                .order(order.get())
                .rating(reviewRequest.getRating())
                .title(reviewRequest.getTitle())
                .comment(reviewRequest.getComment())
                .isVerifiedPurchase(true)
                .build();
        Review saved = reviewRepository.save(review);

        Object[] stats = reviewRepository.getRatingStats(reviewRequest.getProductId());

        BigDecimal avgRating = (BigDecimal) stats[0];
        Integer reviewCount = (Integer) stats[1];

        productRepository.updateRating(reviewRequest.getProductId(), avgRating, reviewCount);
        log.info("created review :{}", reviewRequest.getProductId());
        return ReviewResponse.from(saved);
    }

    @Override
    public ReviewResponse update(Integer id, ReviewRequest reviewRequest) {
        log.info("update review :{}", reviewRequest.getProductId());
        User user = getCurrentUser();
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new NotFoundException(MessageKey.REVIEW_NOT_FOUND)
        );

        if (!review.getUser().getId().equals(user.getId())) {
            throw new PermissionException(MessageKey.CANNOT_UPDATE_REVIEW);
        }

        review.setRating(reviewRequest.getRating());
        review.setTitle(reviewRequest.getTitle());
        review.setComment(reviewRequest.getComment());
        review.setUpdatedAt(LocalDateTime.now());

        reviewRepository.save(review);

        Object[] stats = reviewRepository.getRatingStats(reviewRequest.getProductId());

        BigDecimal avgRating = (BigDecimal) stats[0];
        Integer reviewCount = (Integer) stats[1];

        productRepository.updateRating(reviewRequest.getProductId(), avgRating, reviewCount);
        log.info("updated review :{}", reviewRequest.getProductId());
        return ReviewResponse.from(review);
    }

    @Override
    public void delete(Integer id) {
        log.info("delete review: {}", id);
        User user = getCurrentUser();
        Review review = reviewRepository.findById(id).orElseThrow(
                () -> new NotFoundException(MessageKey.REVIEW_NOT_FOUND)
        );

        if (SecurityUtils.hasRole("ADMIN")){
            reviewRepository.delete(review);
            log.info("admin delete review {}", id);
        }

        if (!review.getUser().getId().equals(user.getId())) {
            throw new PermissionException(MessageKey.CANNOT_UPDATE_REVIEW);
        }
        log.info("delete review: {}", id);
        reviewRepository.delete(review);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getAllReviewByProduct(Integer productId, Pageable pageable) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException(MessageKey.PRODUCT_NOT_FOUND)
        );
        if (!product.getIsActive()){
            throw new InvalidException(MessageKey.PRODUCT_DOESNT_ACTIVE);
        }

        return reviewRepository.findByProduct(product, pageable)
                .map(ReviewResponse::from);

    }

    private User getCurrentUser() {
        Integer userId = SecurityUtils.getCurrentUserId();
        if (userId == null) {
            throw new InvalidException(MessageKey.USER_NOT_LOGIN);
        }

        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(MessageKey.USER_NOT_FOUND)
        );
    }
}
