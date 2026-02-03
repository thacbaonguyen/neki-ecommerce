package com.thacbao.neki.services.impl;

import com.thacbao.neki.dto.request.product.CategoryRequest;
import com.thacbao.neki.dto.request.product.SubCategoryRequest;
import com.thacbao.neki.dto.response.CategoryResponse;
import com.thacbao.neki.dto.response.SubCategoryResponse;
import com.thacbao.neki.exceptions.common.AlreadyException;
import com.thacbao.neki.exceptions.common.InvalidException;
import com.thacbao.neki.exceptions.common.NotFoundException;
import com.thacbao.neki.model.Category;
import com.thacbao.neki.model.SubCategory;
import com.thacbao.neki.repositories.jpa.CategoryRepository;
import com.thacbao.neki.repositories.jpa.SubCategoryRepository;
import com.thacbao.neki.services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        log.info("Creating category: {}", request.getName());

        if (categoryRepository.existsByName(request.getName())) {
            throw new AlreadyException("Danh mục đã tồn tại: " + request.getName());
        }

        String slug = generateSlug(request.getName());
        if (categoryRepository.existsBySlug(slug)) {
            slug = slug + "-" + System.currentTimeMillis();
        }

        Category category = Category.builder()
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        category = categoryRepository.save(category);
        log.info("Category created: {}", category.getName());

        return CategoryResponse.from(category);
    }

    @Override
    public CategoryResponse updateCategory(Integer id, CategoryRequest request) {
        log.info("Updating category ID: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục"));

        if (!category.getName().equals(request.getName())) {
            if (categoryRepository.existsByName(request.getName())) {
                throw new AlreadyException("Tên danh mục đã tồn tại: " + request.getName());
            }

            category.setName(request.getName());

            String slug = generateSlug(request.getName());
            if (categoryRepository.existsBySlug(slug) && !category.getSlug().equals(slug)) {
                slug = slug + "-" + System.currentTimeMillis();
            }
            category.setSlug(slug);
        }

        category.setDescription(request.getDescription());
        category.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : category.getDisplayOrder());
        category.setIsActive(request.getIsActive() != null ? request.getIsActive() : category.getIsActive());

        category = categoryRepository.save(category);
        log.info("Category updated successfully: {}", category.getName());

        return CategoryResponse.from(category);
    }

    @Override
    public void deleteCategory(Integer id) {
        log.info("Deleting category ID: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục"));

        if (!category.getSubCategories().isEmpty()) {
            throw new InvalidException("Không thể xóa danh mục có chứa danh mục con");
        }

        categoryRepository.delete(category);
        log.info("Category deleted successfully: {}", category.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục"));

        return CategoryResponse.from(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục"));

        return CategoryResponse.fromWithSubCategories(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAllActiveOrderByDisplayOrder().stream()
                .map(CategoryResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategoriesWithHierarchy() {
        return categoryRepository.findAllActiveWithSubCategories().stream()
                .map(CategoryResponse::fromWithSubCategories)
                .collect(Collectors.toList());
    }

    @Override
    public void reorderCategories(List<Integer> categoryIds) {
        log.info("Reordering {} categories", categoryIds.size());

        for (int i = 0; i < categoryIds.size(); i++) {
            Integer categoryId = categoryIds.get(i);
            int finalI = i;
            categoryRepository.findById(categoryId).ifPresent(category -> {
                category.setDisplayOrder(finalI);
                categoryRepository.save(category);
            });
        }

        log.info("Categories reordered successfully");
    }

    @Override
    public SubCategoryResponse createSubCategory(SubCategoryRequest request) {
        log.info("Creating subcategory: {}", request.getName());

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục cha"));

        SubCategory parent = null;
        Integer level = 1;

        if (request.getParentId() != null) {
            parent = subCategoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục cha"));

            if (!parent.getCategory().getId().equals(request.getCategoryId())) {
                throw new InvalidException("Danh mục cha phải thuộc cùng danh mục chính");
            }

            level = parent.getLevel() + 1;

            if (level > 3) {
                throw new InvalidException("Không thể tạo danh mục con quá 3cấp");
            }
        }

        if (subCategoryRepository.existsByName(request.getName())) {
            throw new AlreadyException("Tên danh mục con đã tồn tại: " + request.getName());
        }

        String slug = generateSlug(request.getName());
        if (subCategoryRepository.existsBySlug(slug)) {
            slug = slug + "-" + System.currentTimeMillis();
        }

        SubCategory subCategory = SubCategory.builder()
                .category(category)
                .parent(parent)
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .level(level)
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .build();

        subCategory = subCategoryRepository.save(subCategory);
        log.info("SubCategory created: {} (Level {})", subCategory.getName(), level);

        return SubCategoryResponse.from(subCategory);
    }

    @Override
    public SubCategoryResponse updateSubCategory(Integer id, SubCategoryRequest request) {
        log.info("Updating subcategory ID: {}", id);

        SubCategory subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục con"));

        if (!subCategory.getName().equals(request.getName())) {
            if (subCategoryRepository.existsByName(request.getName())) {
                throw new AlreadyException("Tên danh mục con đã tồn tại: " + request.getName());
            }

            subCategory.setName(request.getName());

            String slug = generateSlug(request.getName());
            if (subCategoryRepository.existsBySlug(slug) && !subCategory.getSlug().equals(slug)) {
                slug = slug + "-" + System.currentTimeMillis();
            }
            subCategory.setSlug(slug);
        }

        if (request.getParentId() != null &&
                (subCategory.getParent() == null || !subCategory.getParent().getId().equals(request.getParentId()))) {

            SubCategory newParent = subCategoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục cha"));

            if (newParent.getId().equals(id)) {
                throw new InvalidException("Không thể đặt chính nó làm danh mục cha");
            }

            if (isDescendant(newParent, subCategory)) {
                throw new InvalidException("Không thể đặt danh mục con làm danh mục cha");
            }

            subCategory.setParent(newParent);
            subCategory.setLevel(newParent.getLevel() + 1);

            updateChildrenLevels(subCategory);
        }

        subCategory.setDescription(request.getDescription());
        subCategory.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : subCategory.getDisplayOrder());
        subCategory.setIsActive(request.getIsActive() != null ? request.getIsActive() : subCategory.getIsActive());

        subCategory = subCategoryRepository.save(subCategory);
        log.info("SubCategory updated successfully: {}", subCategory.getName());

        return SubCategoryResponse.from(subCategory);
    }

    @Override
    public void deleteSubCategory(Integer id) {
        log.info("Deleting subcategory ID: {}", id);

        SubCategory subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục con"));

        if (!subCategory.getProducts().isEmpty()) {
            throw new InvalidException("Không thể xóa danh mục con có chứa sản phẩm");
        }

        if (!subCategory.getChildren().isEmpty()) {
            throw new InvalidException("Không thể xóa danh mục con có chứa danh mục con khác");
        }

        subCategoryRepository.delete(subCategory);
        log.info("SubCategory deleted successfully: {}", subCategory.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public SubCategoryResponse getSubCategoryById(Integer id) {
        SubCategory subCategory = subCategoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục con"));

        return SubCategoryResponse.fromWithChildren(subCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public SubCategoryResponse getSubCategoryBySlug(String slug) {
        SubCategory subCategory = subCategoryRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục con"));

        return SubCategoryResponse.fromWithChildren(subCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubCategoryResponse> getSubCategoriesByCategory(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục"));

        return subCategoryRepository.findRootSubCategoriesByCategory(category).stream()
                .map(SubCategoryResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubCategoryResponse> getSubCategoryHierarchy(Integer categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục"));

        return subCategoryRepository.findHierarchyByCategory(category).stream()
                .map(SubCategoryResponse::fromWithChildren)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubCategoryResponse> getSubCategoryChildren(Integer parentId) {
        SubCategory parent = subCategoryRepository.findById(parentId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy danh mục cha"));

        return subCategoryRepository.findChildrenByParent(parent).stream()
                .map(SubCategoryResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    public void reorderSubCategories(Integer parentId, List<Integer> subCategoryIds) {
        log.info("Reordering {} subcategories under parent {}", subCategoryIds.size(), parentId);

        for (int i = 0; i < subCategoryIds.size(); i++) {
            Integer subCategoryId = subCategoryIds.get(i);
            int finalI = i;
            subCategoryRepository.findById(subCategoryId).ifPresent(subCategory -> {
                subCategory.setDisplayOrder(finalI);
                subCategoryRepository.save(subCategory);
            });
        }

        log.info("SubCategories reordered successfully");
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

    private boolean isDescendant(SubCategory potentialDescendant, SubCategory ancestor) {
        SubCategory current = potentialDescendant;
        while (current != null) {
            if (current.getId().equals(ancestor.getId())) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }

    private void updateChildrenLevels(SubCategory parent) {
        for (SubCategory child : parent.getChildren()) {
            child.setLevel(parent.getLevel() + 1);
            subCategoryRepository.save(child);

            if (!child.getChildren().isEmpty()) {
                updateChildrenLevels(child);
            }
        }
    }
}