package com.thacbao.neki.services.impl;

import com.thacbao.neki.dto.response.ProductListResponse;
import com.thacbao.neki.model.*;
import com.thacbao.neki.repositories.jpa.*;
import com.thacbao.neki.services.RecommendationService;
import com.thacbao.neki.services.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationServiceImpl implements RecommendationService {

    private final ProductSimilarityRepository productSimilarityRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final OrderItemRepository orderItemRepository;
    private final WishListRepository wishListRepository;
    private final RedisService redisService;

    private static final String REDIS_PREFIX = "recommendation:similar:";

    private static final double WEIGHT_REVIEW = 1.0;
    private static final double WEIGHT_ORDER = 5.0;
    private static final double WEIGHT_WISHLIST = 3.0;

    @Override
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    // tinh độ tuowgn đồng cosine similarity
    public void calculateSimilarities() {
        log.info("start scheduled similarity");
        long startTime = System.currentTimeMillis();

        try {
            productSimilarityRepository.deleteAllInBatchCustom();

            // user-item matrix
            Map<Integer, Map<Integer, Double>> userMatrix = new HashMap<>();

            // add to user-item matrix
            processReviewsInBatches(userMatrix);
            processOrdersInBatches(userMatrix);
            processWishlistsInBatches(userMatrix);

            if (userMatrix.isEmpty()) {
                log.warn("no data for similarity calculation");
                return;
            }

            // transform to item-user matrix
            Map<Integer, Map<Integer, Double>> itemMatrix = new HashMap<>();
            userMatrix.forEach((userId, items) -> {
                items.forEach((productId, score) -> {
                    itemMatrix.computeIfAbsent(productId, k -> new HashMap<>())
                            .put(userId, score);
                });
            });

            // find active product
            List<Integer> activeProductIds = productRepository.findActiveProductIds(PageRequest.of(0, 5000));
            itemMatrix.keySet().retainAll(new HashSet<>(activeProductIds)); // chi gui lai nhung phan tu co key nam trong activeProductIds

            List<Integer> productIds = new ArrayList<>(itemMatrix.keySet());
            List<ProductSimilarity> similarities = new ArrayList<>();

            // 4. Calculate Similarity between product pairs
            for (int i = 0; i < productIds.size(); i++) {
                for (int j = i + 1; j < productIds.size(); j++) {
                    Integer p1Id = productIds.get(i);
                    Integer p2Id = productIds.get(j);

                    // do tuong dong cua 2 sp ( cosine)
                    double sim = calculateCosineSimilarity(itemMatrix.get(p1Id), itemMatrix.get(p2Id));

                    // check threshold and common > 1
                    if (sim > 0.15 && getCommonUsers(itemMatrix.get(p1Id), itemMatrix.get(p2Id)) >= 2) {
                        Product p1 = new Product();
                        p1.setId(p1Id);
                        Product p2 = new Product();
                        p2.setId(p2Id);

                        // save product similarity P1-> P2 (xScore), P2 ->P1 (xScore)
                        similarities.add(ProductSimilarity.builder().product1(p1).product2(p2).score(sim).build());
                        similarities.add(ProductSimilarity.builder().product1(p2).product2(p1).score(sim).build());
                    }
                }

                // tranh cau quy van qua lon
                if (similarities.size() >= 1000) {
                    productSimilarityRepository.saveAll(similarities);
                    similarities.clear();
                }

            }

            if (!similarities.isEmpty()) {
                productSimilarityRepository.saveAll(similarities);
            }

            redisService.clearCacheUsingScan(REDIS_PREFIX + "*");

            long duration = (System.currentTimeMillis() - startTime) / 1000;
            log.info("similar cal complete in {}s, processs {} products", duration, productIds.size());

        } catch (Exception e) {
            log.error("err in similarity calculate", e);
        }
    }

    // matrix user review-score
    private void processReviewsInBatches(Map<Integer, Map<Integer, Double>> matrix) {
        int page = 0;
        int batchSize = 1000;

        // chay cho den khi het page review
        while (true) {
            Page<Review> reviewPage = reviewRepository.findAll(PageRequest.of(page, batchSize));

            if (reviewPage.isEmpty()) {
                break;
            }

            reviewPage.getContent().forEach(review -> {
                // put score cho user id
                if (review.getRating() >= 3) {
                    matrix.computeIfAbsent(review.getUser().getId(), k -> new HashMap<>())
                            .put(review.getProduct().getId(), review.getRating() * WEIGHT_REVIEW);
                }
            });

            page++;
            if (!reviewPage.hasNext()) {
                break;
            }
        }
        log.debug("processed reviews in {} batches", page);
    }

    //matrix user buy-score (5)
    private void processOrdersInBatches(Map<Integer, Map<Integer, Double>> matrix) {
        int page = 0;
        int batchSize = 1000;

        while (true) {
            Page<OrderItem> orderPage = orderItemRepository.findAll(PageRequest.of(page, batchSize));

            if (orderPage.isEmpty()) {
                break;
            }

            orderPage.getContent().forEach(item -> {
                Integer userId = item.getOrder().getUser().getId();
                Integer productId = item.getVariant().getProduct().getId();

                matrix.computeIfAbsent(userId, k -> new HashMap<>())
                        .merge(productId, WEIGHT_ORDER, Double::max);
            });

            page++;
            if (!orderPage.hasNext()) {
                break;
            }
        }
        log.debug("processed orders in {} batches", page);
    }

    //matrix user wishlist-score
    private void processWishlistsInBatches(Map<Integer, Map<Integer, Double>> matrix) {
        int page = 0;
        int batchSize = 1000;

        while (true) {
            Page<Wishlist> wishlistPage = wishListRepository.findAll(PageRequest.of(page, batchSize));

            if (wishlistPage.isEmpty()) {
                break;
            }

            wishlistPage.getContent().forEach(wishlist -> {
                Integer userId = wishlist.getUser().getId();
                wishlist.getProducts().forEach(product -> {
                    matrix.computeIfAbsent(userId, k -> new HashMap<>())
                            .merge(product.getId(), WEIGHT_WISHLIST, Double::max);
                });
            });

            page++;
            if (!wishlistPage.hasNext()) {
                break;
            }
        }
        log.debug("processed wishlists in {} batches", page);
    }

    //cosine similarity
    private double calculateCosineSimilarity(Map<Integer, Double> v1, Map<Integer, Double> v2) {
        Set<Integer> commonUsers = new HashSet<>(v1.keySet());
        commonUsers.retainAll(v2.keySet());

        if (commonUsers.isEmpty())
            return 0.0;

        double dotProduct = 0.0;
        for (Integer userId : commonUsers) {
            dotProduct += v1.get(userId) * v2.get(userId);
        }

        double norm1 = 0.0;
        for (Double score : v1.values()) {
            norm1 += Math.pow(score, 2);
        }

        double norm2 = 0.0;
        for (Double score : v2.values()) {
            norm2 += Math.pow(score, 2);
        }

        if (norm1 == 0 || norm2 == 0)
            return 0.0;

        return dotProduct / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    //check 1 item >= 2 user-core. check 1 item lon hon 2 user chung cham diem cho sp
    private int getCommonUsers(Map<Integer, Double> v1, Map<Integer, Double> v2) {
        Set<Integer> common = new HashSet<>(v1.keySet());
        common.retainAll(v2.keySet());
        return common.size();
    }

    @Override
    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public Page<ProductListResponse> getSimilarProducts(Integer productId, Pageable pageable) {
        String cacheKey = REDIS_PREFIX + productId;

        List<ProductListResponse> cached = (List<ProductListResponse>) redisService.get(cacheKey);
        if (cached != null) {
            log.debug("return cached rcm for product {}", productId);

            // pagination
            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), cached.size());
            List<ProductListResponse> page = start < cached.size() ? cached.subList(start, end) : Collections.emptyList();

            return new PageImpl<>(page, pageable, cached.size());
        }

        // not exist cache
        List<ProductSimilarity> similarities = productSimilarityRepository.findSimilarProducts(productId,
                PageRequest.of(0, 50));

        List<ProductListResponse> products = similarities.stream()
                .map(ps -> ProductListResponse.from(ps.getProduct2()))
                .collect(Collectors.toList());

        if (!products.isEmpty()) {
            redisService.set(cacheKey, products, 24, TimeUnit.HOURS);
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), products.size());
        List<ProductListResponse> page = start < products.size() ? products.subList(start, end) : Collections.emptyList();

        return new PageImpl<>(page, pageable, products.size());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductListResponse> getRecommendedForYou(Integer userId, int limit) {
        String cacheKey = "recommendation:user:" + userId;

        @SuppressWarnings("unchecked")
        List<ProductListResponse> cached = (List<ProductListResponse>) redisService.get(cacheKey);
        if (cached != null) {
            log.debug("return cached rcm for user {}", userId);
            return cached.stream().limit(limit).collect(Collectors.toList());
        }

        if (userId == null) {
            log.debug("new user {}, rt popular prd", userId);
            List<ProductListResponse> popularProducts = getPopularProducts(limit);
            redisService.set(cacheKey, popularProducts, 1, TimeUnit.HOURS);
            return popularProducts;
        }

        // history interacted
        Set<Integer> userInteractedProducts = getUserInteractedProductIds(userId);

        if (userInteractedProducts.isEmpty()) {
            log.debug("new user {}, rt popular prd", userId);
            List<ProductListResponse> popularProducts = getPopularProducts(limit);
            redisService.set(cacheKey, popularProducts, 1, TimeUnit.HOURS);
            return popularProducts;
        }

        Map<Integer, Double> productScores = new HashMap<>();

        for (Integer interactedProductId : userInteractedProducts) {
            List<ProductSimilarity> similarities = productSimilarityRepository
                    .findSimilarProducts(interactedProductId, PageRequest.of(0, 20));

            similarities.forEach(ps -> {
                Integer recommendedId = ps.getProduct2().getId();

                // k rcm sp user da tuong tac
                if (!userInteractedProducts.contains(recommendedId)) {
                    //dồn điểm
                    productScores.merge(recommendedId, ps.getScore(), Double::sum);
                }
            });
        }

        if (productScores.isEmpty()) {
            log.debug("no similar prd found for userid {}, return popular prod", userId);
            List<ProductListResponse> popularProducts = getPopularProducts(limit);
            redisService.set(cacheKey, popularProducts, 1, TimeUnit.HOURS);
            return popularProducts;
        }

        // sort theo diem va tim va map
        List<ProductListResponse> recommendations = productScores.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .limit(limit * 2)
                .map(entry -> productRepository.findById(entry.getKey()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(Product::getIsActive)
                .map(ProductListResponse::from)
                .limit(limit)
                .collect(Collectors.toList());

        if (!recommendations.isEmpty()) {
            redisService.set(cacheKey, recommendations, 6, TimeUnit.HOURS);
        }

        log.debug("gernerate {} person rcm for user {}", recommendations.size(), userId);
        return recommendations;
    }

    private Set<Integer> getUserInteractedProductIds(Integer userId) {
        Set<Integer> productIds = new HashSet<>();

        int page = 0;
        while (true) {
            Page<OrderItem> orderPage = orderItemRepository
                    .findByOrderUserId(userId, PageRequest.of(page, 100));

            if (orderPage.isEmpty()) {
                break;
            }

            orderPage.getContent().forEach(item ->
                    productIds.add(item.getVariant().getProduct().getId())
            );

            page++;
            if (!orderPage.hasNext()) {
                break;
            }
        }

        Optional<Wishlist> wishlist = wishListRepository.findByUserId(userId);
        wishlist.ifPresent(w ->
                w.getProducts().forEach(p -> productIds.add(p.getId()))
        );

        page = 0;
        while (true) {
            Page<Review> reviewPage = reviewRepository
                    .findByUserIdAndRatingGreaterThanEqual(userId, 4, PageRequest.of(page, 100));

            if (reviewPage.isEmpty()) {
                break;
            }

            reviewPage.getContent().forEach(review ->
                    productIds.add(review.getProduct().getId())
            );

            page++;
            if (!reviewPage.hasNext()) {
                break;
            }
        }

        return productIds;
    }


    private List<ProductListResponse> getPopularProducts(int limit) {
        // best seller and hight rating
        Page<Product> popularProducts = productRepository
                .findByIsActiveTrueOrderByTotalSoldDescAverageRatingDesc(
                        PageRequest.of(0, limit)
                );

        return popularProducts.getContent().stream()
                .map(ProductListResponse::from)
                .collect(Collectors.toList());
    }

}