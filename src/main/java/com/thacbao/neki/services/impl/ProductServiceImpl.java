package com.thacbao.neki.services.impl;
import com.thacbao.neki.dto.request.product.ProductFilterRequest;
import com.thacbao.neki.dto.request.product.ProductImageRequest;
import com.thacbao.neki.dto.request.product.ProductRequest;
import com.thacbao.neki.dto.request.product.ProductVariantRequest;
import com.thacbao.neki.dto.response.*;
import com.thacbao.neki.exceptions.ErrorCode;
import com.thacbao.neki.exceptions.common.*;
import com.thacbao.neki.model.*;
import com.thacbao.neki.model.Collection;
import com.thacbao.neki.repositories.elasticsearch.ProductElasticsearchRepository;
import com.thacbao.neki.repositories.jpa.*;
import com.thacbao.neki.services.CloudinaryService;
import com.thacbao.neki.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final BrandRepository brandRepository;
    private final CollectionRepository collectionRepository;
    private final TopicRepository topicRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final ProductVariantRepository variantRepository;
    private final ProductImageRepository imageRepository;
    private final InventoryRepository inventoryRepository;
    private final CloudinaryService cloudinaryService;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ProductElasticsearchRepository productElasticsearchRepository;

    @Override
    public ProductDetailResponse createProduct(ProductRequest request) {
        log.info("creating : {}", request.getName());

        SubCategory subCategory = subCategoryRepository.findById(request.getSubCategoryId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục con"));

        if (!subCategory.isLeaf()) {
            throw new InvalidException("Sản phẩm phải thuộc danh mục cuối cùng");
        }

        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy brand"));

        if (request.getSalePrice() != null &&
                request.getSalePrice().compareTo(request.getBasePrice()) >= 0) {
            throw new InvalidException("Giá sale không hợp lệ");
        }

        // tao slug
        String slug = generateUniqueSlug(request.getName());

        Product product = Product.builder()
                .subCategory(subCategory)
                .brand(brand)
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .basePrice(request.getBasePrice())
                .salePrice(request.getSalePrice())
                .gender(request.getGender())
                .isFeatured(request.getIsFeatured() != null ? request.getIsFeatured() : false)
                .isNew(request.getIsNew() != null ? request.getIsNew() : false)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .metaTitle(request.getMetaTitle())
                .metaDescription(request.getMetaDescription())
                .metaKeywords(request.getMetaKeywords())
                .build();
        // them collection nếu có
        if (request.getCollectionIds() != null && !request.getCollectionIds().isEmpty()) {
            Set<Collection> collections = new HashSet<>(
                    collectionRepository.findAllById(request.getCollectionIds())
            );
            product.setCollections(collections);
        }
        // them topic nếu có
        if (request.getTopicIds() != null && !request.getTopicIds().isEmpty()) {
            Set<Topic> topics = new HashSet<>(
                    topicRepository.findAllById(request.getTopicIds())
            );
            product.setTopics(topics);
        }

        product = productRepository.save(product);
        //thêm varient nếu có
        if (request.getVariants() != null && !request.getVariants().isEmpty()) {
            for (ProductVariantRequest variantReq : request.getVariants()) {
                createVariantForProduct(product, variantReq);
            }
        }
        //thêm ảnh
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            for (ProductImageRequest imageReq : request.getImages()) {
                addImageToProduct(product, imageReq);
            }
        }

        indexProductToElasticsearch(product);

        log.info("Created: {})", product.getName());

        return ProductDetailResponse.from(productRepository.findById(product.getId())
                .orElseThrow(() -> new NotFoundException("Product not found")));
    }

    @Override
    public ProductDetailResponse updateProduct(Integer id, ProductRequest request) {
        log.info("updating product: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));

        if (!product.getName().equals(request.getName())) {
            product.setName(request.getName());
            String slug = generateUniqueSlug(request.getName());
            if (!product.getSlug().equals(slug)) {
                product.setSlug(slug);
            }
        }

        product.setDescription(request.getDescription());
        product.setBasePrice(request.getBasePrice());
        product.setSalePrice(request.getSalePrice());
        product.setGender(request.getGender());
        product.setIsFeatured(request.getIsFeatured());
        product.setIsNew(request.getIsNew());
        product.setIsActive(request.getIsActive());
        product.setMetaTitle(request.getMetaTitle());
        product.setMetaDescription(request.getMetaDescription());
        product.setMetaKeywords(request.getMetaKeywords());

        // update subcategory
        if (request.getSubCategoryId() != null &&
                !product.getSubCategory().getId().equals(request.getSubCategoryId())) {
            SubCategory subCategory = subCategoryRepository.findById(request.getSubCategoryId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục con"));

            if (!subCategory.isLeaf()) {
                throw new InvalidException("Sản phẩm phải được gán vào danh mục con cuối cùng");
            }

            product.setSubCategory(subCategory);
        }

        // update brand
        if (request.getBrandId() != null &&
                !product.getBrand().getId().equals(request.getBrandId())) {
            Brand brand = brandRepository.findById(request.getBrandId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy thương hiệu"));
            product.setBrand(brand);
        }

        // update collections
        if (request.getCollectionIds() != null) {
            Set<Collection> collections = new HashSet<>(
                    collectionRepository.findAllById(request.getCollectionIds())
            );
            product.setCollections(collections);
        }

        // Update topics
        if (request.getTopicIds() != null) {
            Set<Topic> topics = new HashSet<>(
                    topicRepository.findAllById(request.getTopicIds())
            );
            product.setTopics(topics);
        }

        product = productRepository.save(product);

        indexProductToElasticsearch(product);

        log.info("Product updated: {}", product.getName());

        return ProductDetailResponse.from(product);
    }

    @Override
    public void deleteProduct(Integer id) {
        log.info("Deleting product id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        // deletea all image
        List<ProductImage> images = imageRepository.findByProductOrderByDisplayOrder(product);
        for (ProductImage image : images) {
            cloudinaryService.deleteImage(image.getImageUrl());
        }

        // delete from elastic
        try {
            elasticsearchOperations.delete(id.toString(), Product.class);
        } catch (Exception e) {
            log.error("Failed to delete from Elasticsearch: {}", id, e);
        }

        // Delete product (cascade will delete variants, images....)
        productRepository.delete(product);

        log.info("Product deleted: {}", product.getName());
    }

    @Override
    public void toggleProductStatus(Integer id, boolean isActive) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        product.setIsActive(isActive);
        productRepository.save(product);

        if (isActive) {
            indexProductToElasticsearch(product);
        } else {
            elasticsearchOperations.delete(id.toString(), Product.class);
        }

        log.info("Product {} status: {}", id, isActive ? "activated" : "deactivated");
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetailResponse getProductByIdAdmin(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        return ProductDetailResponse.from(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductListResponse> getAllProductsAdmin(ProductFilterRequest filter, Pageable pageable) {
        Page<Product> products = productRepository.filterProducts(filter, pageable);
        return products.map(ProductListResponse::from);
    }

    // Product Images

    @Override
    public ProductImageResponse addProductImage(Integer productId, MultipartFile file,
                                                Integer colorId, Integer displayOrder, Boolean isPrimary) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        // Upload to Cloudinary
        String imageUrl = cloudinaryService.uploadImage(file, "products/" + productId);

        ProductImageRequest request = ProductImageRequest.builder()
                .imageUrl(imageUrl)
                .colorId(colorId)
                .displayOrder(displayOrder != null ? displayOrder : 0)
                .isPrimary(isPrimary != null ? isPrimary : false)
                .build();

        addImageToProduct(product, request);

        ProductImage savedImage = imageRepository.findByProductOrderByDisplayOrder(product)
                .stream()
                .filter(img -> img.getImageUrl().equals(imageUrl))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Image not found"));

        log.info("Image added to product {}", productId);

        return ProductImageResponse.from(savedImage);
    }

    @Override
    public ProductImageResponse addProductImageByUrl(Integer productId, ProductImageRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        addImageToProduct(product, request);

        ProductImage savedImage = imageRepository.findByProductOrderByDisplayOrder(product)
                .stream()
                .filter(img -> img.getImageUrl().equals(request.getImageUrl()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Image not found"));

        log.info("Image URL added to product {}", productId);

        return ProductImageResponse.from(savedImage);
    }

    @Override
    public void deleteProductImage(Integer imageId) {
        ProductImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy ảnh"));

        // Delete from Cloudinary
        cloudinaryService.deleteImage(image.getImageUrl());

        // Delete from database
        imageRepository.delete(image);

        log.info("Product image deleted: {}", imageId);
    }

    @Override
    public void updateImageOrder(Integer productId, List<Integer> imageIds) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        for (int i = 0; i < imageIds.size(); i++) {
            Integer imageId = imageIds.get(i);
            int finalI = i;
            imageRepository.findById(imageId).ifPresent(image -> {
                if (image.getProduct().getId().equals(productId)) {
                    image.setDisplayOrder(finalI);
                    imageRepository.save(image);
                }
            });
        }

        log.info("Image order updated for product {}", productId);
    }

    @Override
    public void setPrimaryImage(Integer imageId) {
        ProductImage image = imageRepository.findById(imageId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy ảnh"));

        // Unset other primary images
        imageRepository.findByProductOrderByDisplayOrder(image.getProduct())
                .forEach(img -> {
                    img.setIsPrimary(false);
                    imageRepository.save(img);
                });

        // Set this as primary
        image.setIsPrimary(true);
        imageRepository.save(image);

        log.info("Primary image set: {}", imageId);
    }

    @Override
    public ProductVariantResponse addProductVariant(Integer productId, ProductVariantRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        createVariantForProduct(product, request);

        // Find the created variant
        Color color = colorRepository.findById(request.getColorId()).orElseThrow();
        Size size = sizeRepository.findById(request.getSizeId()).orElseThrow();

        ProductVariant variant = variantRepository.findByProductAndColorAndSize(product, color, size)
                .orElseThrow(() -> new NotFoundException("Variant not found"));

        log.info("Variant added to product {}", productId);

        return ProductVariantResponse.from(variant, product.getCurrentPrice());
    }

    @Override
    public ProductVariantResponse updateProductVariant(Integer variantId, ProductVariantRequest request) {
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phiên bản sản phẩm"));

        // upd if change
        if (request.getColorId() != null && !variant.getColor().getId().equals(request.getColorId())) {
            Color color = colorRepository.findById(request.getColorId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy màu sắc"));
            variant.setColor(color);
        }

        // Update if change
        if (request.getSizeId() != null && !variant.getSize().getId().equals(request.getSizeId())) {
            Size size = sizeRepository.findById(request.getSizeId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy kích thước"));
            variant.setSize(size);
        }

        variant.setAdditionalPrice(request.getAdditionalPrice() != null ? request.getAdditionalPrice() : variant.getAdditionalPrice());
        variant.setIsActive(request.getIsActive() != null ? request.getIsActive() : variant.getIsActive());

        variant = variantRepository.save(variant);

        log.info("Variant updated: {}", variantId);

        return ProductVariantResponse.from(variant, variant.getProduct().getCurrentPrice());
    }

    @Override
    public void deleteProductVariant(Integer variantId) {
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phiên bản sản phẩm"));

        variantRepository.delete(variant);

        log.info("Variant deleted: {}", variantId);
    }

    @Override
    public void toggleVariantStatus(Integer variantId, boolean isActive) {
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phiên bản sản phẩm"));

        variant.setIsActive(isActive);
        variantRepository.save(variant);

        log.info("Variant {} status: {}", variantId, isActive ? "activated" : "deactivated");
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariantResponse> getProductVariants(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        return variantRepository.findByProductWithInventory(product).stream()
                .map(v -> ProductVariantResponse.from(v, product.getCurrentPrice()))
                .collect(Collectors.toList());
    }

// Inventory Management
    /**
     * update kho hàng
     * */
    @Override
    public void updateInventory(Integer variantId, Integer quantity) {
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phiên bản sản phẩm"));

        Inventory inventory = inventoryRepository.findByVariant(variant)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy kho hàng"));

        inventory.setQuantity(quantity);
        inventory.setLastRestockedAt(LocalDateTime.now());
        inventoryRepository.save(inventory);

        log.info("Inventory updated for variant {}: {} units", variantId, quantity);
    }
    /**
     * Giữ hàng tạm thời
     * */
    @Override
    public void reserveInventory(Integer variantId, Integer quantity) {
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phiên bản sản phẩm"));

        Inventory inventory = inventoryRepository.findByVariant(variant)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy kho hàng"));

        int available = inventory.getQuantity() - inventory.getReservedQuantity();
        if (available < quantity) {
            throw new InvalidException("Không đủ hàng trong kho. Còn lại: " + available);
        }

        // Tăng reverse inventory
        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
        inventoryRepository.save(inventory);

        log.info("Reserved {} units for variant {}", quantity, variantId);
    }

    @Override
    public void restoreReserveInventory(Integer variantId, Integer quantity) {
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phiên bản sản phẩm"));

        Inventory inventory = inventoryRepository.findByVariant(variant)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy kho hàng"));

        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
        inventoryRepository.save(inventory);
    }

    /**
     * Trả lại hàng đã giữ (khi hủy / timeout)
     * */
    @Override
    public void releaseInventory(Integer variantId, Integer quantity) {
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phiên bản sản phẩm"));

        Inventory inventory = inventoryRepository.findByVariant(variant)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy kho hàng"));

        // tang lai so sp
        inventory.setQuantity(inventory.getQuantity() + quantity);

        inventoryRepository.save(inventory);

        log.info("Released {} units for variant {}", quantity, variantId);
    }

    /**
     * Trừ kho thật
     * */
    @Override
    public void confirmInventory(Integer variantId, Integer quantity) {
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phiên bản sản phẩm"));

        Inventory inventory = inventoryRepository.findByVariant(variant)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy kho hàng"));

        inventory.setReservedQuantity(
                Math.max(0, inventory.getReservedQuantity() - quantity)
        );
        inventory.setQuantity(
                Math.max(0, inventory.getQuantity() - quantity)
        );

        inventoryRepository.save(inventory);

        log.info("Confirmed purchase of {} units for variant {}", quantity, variantId);
    }

    /**
     * nhập thêm số lượng
     * */
    @Override
    public void adjustInventory(Integer variantId, Integer quantity, String reason) {
        ProductVariant variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy phiên bản sản phẩm"));

        Inventory inventory = inventoryRepository.findByVariant(variant)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy kho hàng"));

        int oldQuantity = inventory.getQuantity();
        inventory.setQuantity(Math.max(0, oldQuantity + quantity));
        inventoryRepository.save(inventory);

        log.info("Inventory adjusted for variant {}: {} -> {} (Reason: {})",
                variantId, oldQuantity, inventory.getQuantity(), reason);
    }

// Public API

    @Override
    @Transactional
    public ProductDetailResponse getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        if (!product.getIsActive()) {
            throw new NotFoundException("Sản phẩm không khả dụng");
        }

        // Increment view count
        productRepository.incrementViewCount(id);

        return ProductDetailResponse.from(product);
    }

    @Override
    @Transactional
    public ProductDetailResponse getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        if (!product.getIsActive()) {
            throw new NotFoundException("Sản phẩm không khả dụng");
        }

        // Increment view count
        productRepository.incrementViewCount(product.getId());

        return ProductDetailResponse.from(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductListResponse> filterProducts(ProductFilterRequest filter, Pageable pageable) {
        Page<Product> products = productRepository.filterProducts(filter, pageable);
        return products.map(ProductListResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductListResponse> searchProducts(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return Page.empty();
        }

        // Use Elasticsearch for full-text search
        try {
            // Simple implementation - you may need to adjust based on your Elasticsearch setup
            Page<Product> products = productElasticsearchRepository.searchProducts(keyword, pageable);
            return products.map(ProductListResponse::from);
        } catch (Exception e) {
            log.error("Elasticsearch search failed, falling back to database search", e);
            ProductFilterRequest filter = ProductFilterRequest.builder()
                    .keyword(keyword)
                    .build();
            return filterProducts(filter, pageable);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductListResponse> getRelatedProducts(Integer productId, int limit) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.PRODUCT_NOT_FOUND));

        List<Product> relatedProducts = productRepository.findRelatedProducts(
                productId, product.getSubCategory().getId(), limit);

        return relatedProducts.stream()
                .map(ProductListResponse::from)
                .collect(Collectors.toList());
    }

//Special Listings

    @Override
    @Transactional(readOnly = true)
    public Page<ProductListResponse> getFeaturedProducts(Pageable pageable) {
        Page<Product> products = productRepository.findByIsFeaturedTrueAndIsActiveTrue(pageable);
        return products.map(ProductListResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductListResponse> getNewProducts(Pageable pageable) {
        Page<Product> products = productRepository.findByIsNewTrueAndIsActiveTrue(pageable);
        return products.map(ProductListResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductListResponse> getOnSaleProducts(Pageable pageable) {
        Page<Product> products = productRepository.findOnSaleProducts(pageable);
        return products.map(ProductListResponse::from);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductListResponse> getBestSellers(Pageable pageable) {
        Page<Product> products = productRepository.findBestSellers(pageable);
        return products.map(ProductListResponse::from);
    }

// Filter Options

    @Override
    @Transactional(readOnly = true)
    public FilterOptionsResponse getFilterOptions(Integer categoryId) {
        FilterOptionsResponse.FilterOptionsResponseBuilder builder = FilterOptionsResponse.builder();

        // Get all categories
        List<CategoryResponse> categories = categoryRepository.findByIsActiveTrue().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
        builder.categories(categories);

        // Get all brands
        List<BrandResponse> brands = brandRepository.findByIsActiveTrue().stream()
                .map(BrandResponse::from)
                .collect(Collectors.toList());
        builder.brands(brands);

        // Get all collections
        List<CollectionResponse> collections = collectionRepository.findByIsActiveTrue().stream()
                .map(CollectionResponse::from)
                .collect(Collectors.toList());
        builder.collections(collections);

        // Get all topics
        List<TopicResponse> topics = topicRepository.findByIsActiveTrueOrderByName().stream()
                .map(TopicResponse::from)
                .collect(Collectors.toList());
        builder.topics(topics);

        // Get all colors
        List<ColorResponse> colors = colorRepository.findAllByOrderByName().stream()
                .map(ColorResponse::from)
                .collect(Collectors.toList());
        builder.colors(colors);

        // Get all sizes
        List<SizeResponse> sizes = sizeRepository.findAllByOrderByCategoryTypeAscDisplayOrderAsc().stream()
                .map(SizeResponse::from)
                .collect(Collectors.toList());
        builder.sizes(sizes);

        // Get price range
        if (categoryId != null) {
            BigDecimal[] priceRange = productRepository.getPriceRangeByCategory(categoryId);
            builder.minPrice(priceRange[0]);
            builder.maxPrice(priceRange[1]);
        }

        return builder.build();
    }

// ========== Bulk Operations ==========

    @Override
    public void bulkUpdateStatus(List<Integer> productIds, boolean isActive) {
        List<Product> products = productRepository.findAllById(productIds);

        products.forEach(product -> {
            product.setIsActive(isActive);
            productRepository.save(product);
            indexProductToElasticsearch(product);
        });

        log.info("Bulk status update: {} products {}", productIds.size(), isActive ? "activated" : "deactivated");
    }

    @Override
    public void bulkDelete(List<Integer> productIds) {
        List<Product> products = productRepository.findAllById(productIds);

        products.forEach(product -> {
            // Delete images from Cloudinary
            imageRepository.findByProductOrderByDisplayOrder(product)
                    .forEach(image -> cloudinaryService.deleteImage(image.getImageUrl()));

            // Delete from Elasticsearch
            try {
                elasticsearchOperations.delete(product.getId().toString(), Product.class);
            } catch (Exception e) {
                log.error("Failed to delete from Elasticsearch: {}", product.getId(), e);
            }
        });

        productRepository.deleteAll(products);

        log.info("Bulk delete: {} products", productIds.size());
    }

    // Helper Methods

    private void createVariantForProduct(Product product, ProductVariantRequest request) {
        Color color = colorRepository.findById(request.getColorId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy mau"));
        Size size = sizeRepository.findById(request.getSizeId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy size"));

        // Check duplicate
        if (variantRepository.existsByProductAndColorAndSize(product, color, size)) {
            throw new AlreadyException("Phiên bản sản phẩm đã tồn tại với màu và size này");
        }

        ProductVariant variant = ProductVariant.builder()
                .product(product)
                .color(color)
                .size(size)
                .additionalPrice(request.getAdditionalPrice() != null ? request.getAdditionalPrice() : BigDecimal.ZERO)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        variant = variantRepository.save(variant);

        // Create inventory
        Inventory inventory = Inventory.builder()
                .variant(variant)
                .quantity(request.getQuantity() != null ? request.getQuantity() : 0)
                .reservedQuantity(0)
                .lastRestockedAt(LocalDateTime.now())
                .build();

        inventoryRepository.save(inventory);
    }

    private void addImageToProduct(Product product, ProductImageRequest request) {
        Color color = null;
        if (request.getColorId() != null) {
            color = colorRepository.findById(request.getColorId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy màu sắc"));
        }

        // thay doi primary moi
        if (Boolean.TRUE.equals(request.getIsPrimary())) {
            imageRepository.findByProductOrderByDisplayOrder(product)
                    .forEach(img -> {
                        img.setIsPrimary(false);
                        imageRepository.save(img);
                    });
        }

        ProductImage image = ProductImage.builder()
                .product(product)
                .color(color)
                .imageUrl(request.getImageUrl())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .isPrimary(request.getIsPrimary() != null ? request.getIsPrimary() : false)
                .build();

        imageRepository.save(image);
    }

    private String generateUniqueSlug(String name) {
        String slug = name.toLowerCase()
                .replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a")
                .replaceAll("[èéẹẻẽêềếệểễ]", "e")
                .replaceAll("[ìíịỉĩ]", "i")
                .replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o")
                .replaceAll("[ùúụủũưừứựửữ]", "u")
                .replaceAll("[ỳýỵỷỹ]", "y")
                .replaceAll("[đ]", "d")
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");

        // Ensure uniqueness
        if (productRepository.existsBySlug(slug)) {
            slug = slug + "-" + System.currentTimeMillis();
        }

        return slug;
    }

    private void indexProductToElasticsearch(Product product) {
        try {
            elasticsearchOperations.save(product);
            log.info("Product indexed to Elasticsearch: {}", product.getId());
        } catch (Exception e) {
            log.error("Failed to index product to Elasticsearch: {}", product.getId(), e);
        }
    }
}

