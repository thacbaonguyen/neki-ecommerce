package com.thacbao.neki.services.impl;

import com.thacbao.neki.dto.request.product.*;
import com.thacbao.neki.dto.response.*;
import com.thacbao.neki.exceptions.common.*;
import com.thacbao.neki.model.*;
import com.thacbao.neki.repositories.jpa.*;
import com.thacbao.neki.services.AttributeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AttributeServiceImpl implements AttributeService {

    private final BrandRepository brandRepository;
    private final CollectionRepository collectionRepository;
    private final TopicRepository topicRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final SubCategoryRepository subCategoryRepository;
    
    @Override
    public BrandResponse createBrand(BrandRequest request) {
        log.info("Creating brand: {}", request.getName());

        if (brandRepository.existsByName(request.getName())) {
            throw new AlreadyException("Brand đã tồn tại: " + request.getName());
        }

        Brand brand = Brand.builder()
                .name(request.getName())
                .description(request.getDescription())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        brand = brandRepository.save(brand);
        log.info("Brand created: {}", brand.getName());

        return BrandResponse.from(brand);
    }

    @Override
    public BrandResponse updateBrand(Integer id, BrandRequest request) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Brand"));

        if (!brand.getName().equals(request.getName()) && brandRepository.existsByName(request.getName())) {
            throw new AlreadyException("Tên Brand đã tồn tại");
        }

        brand.setName(request.getName());
        brand.setDescription(request.getDescription());
        brand.setIsActive(request.getIsActive() != null ? request.getIsActive() : brand.getIsActive());

        brand = brandRepository.save(brand);
        log.info("Brand updated: {}", brand.getName());

        return BrandResponse.from(brand);
    }

    @Override
    public void deleteBrand(Integer id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Brand"));

        if (!brand.getProducts().isEmpty()) {
            throw new InvalidException("Không thể xóa Brand có sản phẩm");
        }

        brandRepository.delete(brand);
        log.info("Brand deleted: {}", brand.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public BrandResponse getBrandById(Integer id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Brand"));
        return BrandResponse.from(brand);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrandResponse> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(BrandResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrandResponse> getAllActiveBrands() {
        return brandRepository.findByIsActiveTrue().stream()
                .map(BrandResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public CollectionResponse createCollection(CollectionRequest request) {
        log.info("Creating collection: {}", request.getName());

        if (collectionRepository.existsByName(request.getName())) {
            throw new AlreadyException("Collection đã tồn tại: " + request.getName());
        }

        String slug = generateSlug(request.getName());
        if (collectionRepository.existsBySlug(slug)) {
            slug = slug + "-" + System.currentTimeMillis();
        }

        Collection collection = Collection.builder()
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        // Add subcategories
        if (request.getSubCategoryIds() != null && !request.getSubCategoryIds().isEmpty()) {
            Set<SubCategory> subCategories = new HashSet<>(
                    subCategoryRepository.findAllById(request.getSubCategoryIds())
            );
            collection.setSubCategories(subCategories);
        }

        collection = collectionRepository.save(collection);
        log.info("Collection created: {}", collection.getName());

        return CollectionResponse.from(collection);
    }

    @Override
    public CollectionResponse updateCollection(Integer id, CollectionRequest request) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Collection"));

        if (!collection.getName().equals(request.getName())) {
            if (collectionRepository.existsByName(request.getName())) {
                throw new AlreadyException("Tên Collection đã tồn tại");
            }

            collection.setName(request.getName());
            String slug = generateSlug(request.getName());
            if (!collection.getSlug().equals(slug)) {
                collection.setSlug(slug);
            }
        }

        collection.setDescription(request.getDescription());
        collection.setIsActive(request.getIsActive() != null ? request.getIsActive() : collection.getIsActive());

        // Update subcategories
        if (request.getSubCategoryIds() != null) {
            Set<SubCategory> subCategories = new HashSet<>(
                    subCategoryRepository.findAllById(request.getSubCategoryIds())
            );
            collection.setSubCategories(subCategories);
        }

        collection = collectionRepository.save(collection);
        log.info("Collection updated: {}", collection.getName());

        return CollectionResponse.from(collection);
    }

    @Override
    public void deleteCollection(Integer id) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Collection"));

        collectionRepository.delete(collection);
        log.info("Collection deleted: {}", collection.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public CollectionResponse getCollectionById(Integer id) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Collection"));
        return CollectionResponse.from(collection);
    }

    @Override
    @Transactional(readOnly = true)
    public CollectionResponse getCollectionBySlug(String slug) {
        Collection collection = collectionRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Collection"));
        return CollectionResponse.from(collection);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CollectionResponse> getAllCollections() {
        return collectionRepository.findAll().stream()
                .map(CollectionResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CollectionResponse> getAllActiveCollections() {
        return collectionRepository.findByIsActiveTrue().stream()
                .map(CollectionResponse::from)
                .collect(Collectors.toList());
    }
// topic
    @Override
    public TopicResponse createTopic(TopicRequest request) {
        log.info("Creating topic: {}", request.getName());

        if (topicRepository.existsByName(request.getName())) {
            throw new AlreadyException("Topic đã tồn tại: " + request.getName());
        }

        String slug = generateSlug(request.getName());
        if (topicRepository.existsBySlug(slug)) {
            slug = slug + "-" + System.currentTimeMillis();
        }

        Topic topic = Topic.builder()
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        topic = topicRepository.save(topic);
        log.info("Topic created: {}", topic.getName());

        return TopicResponse.from(topic);
    }

    @Override
    public TopicResponse updateTopic(Integer id, TopicRequest request) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Topic"));

        if (!topic.getName().equals(request.getName())) {
            if (topicRepository.existsByName(request.getName())) {
                throw new AlreadyException("Tên Topic đã tồn tại");
            }

            topic.setName(request.getName());
            String slug = generateSlug(request.getName());
            if (!topic.getSlug().equals(slug)) {
                topic.setSlug(slug);
            }
        }

        topic.setDescription(request.getDescription());
        topic.setIsActive(request.getIsActive() != null ? request.getIsActive() : topic.getIsActive());

        topic = topicRepository.save(topic);
        log.info("Topic updated: {}", topic.getName());

        return TopicResponse.from(topic);
    }

    @Override
    public void deleteTopic(Integer id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Topic"));

        topicRepository.delete(topic);
        log.info("Topic deleted: {}", topic.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public TopicResponse getTopicById(Integer id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Topic"));
        return TopicResponse.from(topic);
    }

    @Override
    @Transactional(readOnly = true)
    public TopicResponse getTopicBySlug(String slug) {
        Topic topic = topicRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Topic"));
        return TopicResponse.from(topic);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopicResponse> getAllTopics() {
        return topicRepository.findAll().stream()
                .map(TopicResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopicResponse> getAllActiveTopics() {
        return topicRepository.findByIsActiveTrueOrderByName().stream()
                .map(TopicResponse::from)
                .collect(Collectors.toList());
    }

    // Color

    @Override
    public ColorResponse createColor(ColorRequest request) {
        log.info("Creating color: {}", request.getName());

        if (colorRepository.existsByName(request.getName())) {
            throw new AlreadyException("Color đã tồn tại: " + request.getName());
        }

        if (colorRepository.existsByHexCode(request.getHexCode())) {
            throw new AlreadyException("Mã màu hex đã tồn tại: " + request.getHexCode());
        }

        Color color = Color.builder()
                .name(request.getName())
                .hexCode(request.getHexCode())
                .build();

        color = colorRepository.save(color);
        log.info("Color created: {}", color.getName());

        return ColorResponse.from(color);
    }

    @Override
    public ColorResponse updateColor(Integer id, ColorRequest request) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Color"));

        if (!color.getName().equals(request.getName()) && colorRepository.existsByName(request.getName())) {
            throw new AlreadyException("Tên Color đã tồn tại");
        }

        if (!color.getHexCode().equals(request.getHexCode()) && colorRepository.existsByHexCode(request.getHexCode())) {
            throw new AlreadyException("Mã màu hex đã tồn tại");
        }

        color.setName(request.getName());
        color.setHexCode(request.getHexCode());

        color = colorRepository.save(color);
        log.info("Color updated: {}", color.getName());

        return ColorResponse.from(color);
    }

    @Override
    public void deleteColor(Integer id) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Color"));

        colorRepository.delete(color);
        log.info("Color deleted: {}", color.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public ColorResponse getColorById(Integer id) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Color"));
        return ColorResponse.from(color);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ColorResponse> getAllColors() {
        return colorRepository.findAllByOrderByName().stream()
                .map(ColorResponse::from)
                .collect(Collectors.toList());
    }

    // Size

    @Override
    public SizeResponse createSize(SizeRequest request) {
        log.info("Creating size: {} for {}", request.getName(), request.getCategoryType());

        if (sizeRepository.existsByNameAndCategoryType(request.getName(), request.getCategoryType())) {
            throw new AlreadyException("Size đã tồn tại cho loại danh mục này");
        }

        Size size = Size.builder()
                .name(request.getName())
                .categoryType(request.getCategoryType())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .build();

        size = sizeRepository.save(size);
        log.info("Size created: {}", size.getName());

        return SizeResponse.from(size);
    }

    @Override
    public SizeResponse updateSize(Integer id, SizeRequest request) {
        Size size = sizeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy size"));

        if ((!size.getName().equals(request.getName()) || !size.getCategoryType().equals(request.getCategoryType()))
                && sizeRepository.existsByNameAndCategoryType(request.getName(), request.getCategoryType())) {
            throw new AlreadyException("Size đã tồn tại cho loại danh mục này");
        }

        size.setName(request.getName());
        size.setCategoryType(request.getCategoryType());
        size.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : size.getDisplayOrder());

        size = sizeRepository.save(size);
        log.info("Size updated: {}", size.getName());

        return SizeResponse.from(size);
    }

    @Override
    public void deleteSize(Integer id) {
        Size size = sizeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy size"));

        sizeRepository.delete(size);
        log.info("Size deleted: {}", size.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public SizeResponse getSizeById(Integer id) {
        Size size = sizeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy size"));
        return SizeResponse.from(size);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SizeResponse> getSizesByCategoryType(String categoryType) {
        return sizeRepository.findByCategoryTypeOrderByDisplayOrder(categoryType).stream()
                .map(SizeResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SizeResponse> getAllSizes() {
        return sizeRepository.findAllByOrderByCategoryTypeAscDisplayOrderAsc().stream()
                .map(SizeResponse::from)
                .collect(Collectors.toList());
    }

    private String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a")
                .replaceAll("[èéẹẻẽêềếệểễ]", "e")
                .replaceAll("[ìíịỉĩ]", "i")
                .replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o")
                .replaceAll("[ùúụủũưừứựửữ]", "u")
                .replaceAll("[ỳýỵỷỹ]", "y")
                .replaceAll("[đ]", "d")
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }
}