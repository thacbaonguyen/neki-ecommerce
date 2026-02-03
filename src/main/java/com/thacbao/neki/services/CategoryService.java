package com.thacbao.neki.services;


import com.thacbao.neki.dto.request.product.CategoryRequest;
import com.thacbao.neki.dto.request.product.SubCategoryRequest;
import com.thacbao.neki.dto.response.CategoryResponse;
import com.thacbao.neki.dto.response.SubCategoryResponse;

import java.util.List;

public interface CategoryService {

    // ========== Category CRUD ==========

    /**
     * Create new category
     */
    CategoryResponse createCategory(CategoryRequest request);

    /**
     * Update category
     */
    CategoryResponse updateCategory(Integer id, CategoryRequest request);

    /**
     * Delete category
     */
    void deleteCategory(Integer id);

    /**
     * Get category by ID
     */
    CategoryResponse getCategoryById(Integer id);

    /**
     * Get category by slug
     */
    CategoryResponse getCategoryBySlug(String slug);

    /**
     * Get all categories
     */
    List<CategoryResponse> getAllCategories();

    /**
     * Get all active categories with full hierarchy
     */
    List<CategoryResponse> getAllCategoriesWithHierarchy();

    /**
     * Reorder categories
     */
    void reorderCategories(List<Integer> categoryIds);

    // ========== SubCategory CRUD ==========

    /**
     * Create new subcategory
     */
    SubCategoryResponse createSubCategory(SubCategoryRequest request);

    /**
     * Update subcategory
     */
    SubCategoryResponse updateSubCategory(Integer id, SubCategoryRequest request);

    /**
     * Delete subcategory
     */
    void deleteSubCategory(Integer id);

    /**
     * Get subcategory by ID
     */
    SubCategoryResponse getSubCategoryById(Integer id);

    /**
     * Get subcategory by slug
     */
    SubCategoryResponse getSubCategoryBySlug(String slug);

    /**
     * Get all subcategories of a category (root level only)
     */
    List<SubCategoryResponse> getSubCategoriesByCategory(Integer categoryId);

    /**
     * Get full subcategory hierarchy for a category
     */
    List<SubCategoryResponse> getSubCategoryHierarchy(Integer categoryId);

    /**
     * Get children of a parent subcategory
     */
    List<SubCategoryResponse> getSubCategoryChildren(Integer parentId);

    /**
     * Reorder subcategories within same parent
     */
    void reorderSubCategories(Integer parentId, List<Integer> subCategoryIds);
}