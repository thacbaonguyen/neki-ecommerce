package com.thacbao.neki.controllers.pub;

import com.thacbao.neki.dto.response.ApiResponse;
import com.thacbao.neki.dto.response.CategoryResponse;
import com.thacbao.neki.dto.response.SubCategoryResponse;
import com.thacbao.neki.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategoriesWithHierarchy();
        return ResponseEntity.ok(
                ApiResponse.<List<CategoryResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(categories)
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Integer id) {
        CategoryResponse category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(
                ApiResponse.<CategoryResponse>builder()
                        .code(200)
                        .status("success")
                        .data(category)
                        .build()
        );
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryBySlug(@PathVariable String slug) {
        CategoryResponse category = categoryService.getCategoryBySlug(slug);
        return ResponseEntity.ok(
                ApiResponse.<CategoryResponse>builder()
                        .code(200)
                        .status("success")
                        .data(category)
                        .build()
        );
    }

    @GetMapping("/{id}/subcategories")
    public ResponseEntity<ApiResponse<List<SubCategoryResponse>>> getSubCategoryHierarchy(
            @PathVariable Integer id) {
        List<SubCategoryResponse> subCategories = categoryService.getSubCategoryHierarchy(id);
        return ResponseEntity.ok(
                ApiResponse.<List<SubCategoryResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(subCategories)
                        .build()
        );
    }

    @GetMapping("/subcategories/{id}")
    public ResponseEntity<ApiResponse<SubCategoryResponse>> getSubCategoryById(@PathVariable Integer id) {
        SubCategoryResponse subCategory = categoryService.getSubCategoryById(id);
        return ResponseEntity.ok(
                ApiResponse.<SubCategoryResponse>builder()
                        .code(200)
                        .status("success")
                        .data(subCategory)
                        .build()
        );
    }

    @GetMapping("/subcategories/slug/{slug}")
    public ResponseEntity<ApiResponse<SubCategoryResponse>> getSubCategoryBySlug(@PathVariable String slug) {
        SubCategoryResponse subCategory = categoryService.getSubCategoryBySlug(slug);
        return ResponseEntity.ok(
                ApiResponse.<SubCategoryResponse>builder()
                        .code(200)
                        .status("success")
                        .data(subCategory)
                        .build()
        );
    }
}