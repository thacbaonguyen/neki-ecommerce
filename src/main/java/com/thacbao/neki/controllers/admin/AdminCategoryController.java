package com.thacbao.neki.controllers.admin;

import com.thacbao.neki.dto.request.product.CategoryRequest;
import com.thacbao.neki.dto.request.product.SubCategoryRequest;
import com.thacbao.neki.dto.response.ApiResponse;
import com.thacbao.neki.dto.response.CategoryResponse;
import com.thacbao.neki.dto.response.SubCategoryResponse;
import com.thacbao.neki.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/categories")
@RequiredArgsConstructor
@Validated
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest request) {
        CategoryResponse category = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<CategoryResponse>builder()
                        .code(201)
                        .status("success")
                        .message("Tạo Category thành công")
                        .data(category)
                        .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Integer id,
            @Valid @RequestBody CategoryRequest request) {
        CategoryResponse category = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(
                ApiResponse.<CategoryResponse>builder()
                        .code(200)
                        .status("success")
                        .message("Cập nhật Category thành công")
                        .data(category)
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Xóa Category thành công")
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

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(
                ApiResponse.<List<CategoryResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(categories)
                        .build()
        );
    }

    @PostMapping("/reorder")
    public ResponseEntity<ApiResponse<Void>> reorderCategories(
            @RequestBody List<Integer> categoryIds) {
        categoryService.reorderCategories(categoryIds);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Sắp xếp lại Category thành công")
                        .build()
        );
    }

    @PostMapping("/{categoryId}/subcategories")
    public ResponseEntity<ApiResponse<SubCategoryResponse>> createSubCategory(
            @PathVariable Integer categoryId,
            @Valid @RequestBody SubCategoryRequest request) {
        request.setCategoryId(categoryId);
        SubCategoryResponse subCategory = categoryService.createSubCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<SubCategoryResponse>builder()
                        .code(201)
                        .status("success")
                        .message("Tạo Category con thành công")
                        .data(subCategory)
                        .build()
        );
    }

    @PutMapping("/subcategories/{id}")
    public ResponseEntity<ApiResponse<SubCategoryResponse>> updateSubCategory(
            @PathVariable Integer id,
            @Valid @RequestBody SubCategoryRequest request) {
        SubCategoryResponse subCategory = categoryService.updateSubCategory(id, request);
        return ResponseEntity.ok(
                ApiResponse.<SubCategoryResponse>builder()
                        .code(200)
                        .status("success")
                        .message("Cập nhật Category con thành công")
                        .data(subCategory)
                        .build()
        );
    }

    @DeleteMapping("/subcategories/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSubCategory(@PathVariable Integer id) {
        categoryService.deleteSubCategory(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Xóa Category con thành công")
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

    @GetMapping("/{categoryId}/subcategories")
    public ResponseEntity<ApiResponse<List<SubCategoryResponse>>> getSubCategories(
            @PathVariable Integer categoryId) {
        List<SubCategoryResponse> subCategories = categoryService.getSubCategoriesByCategory(categoryId);
        return ResponseEntity.ok(
                ApiResponse.<List<SubCategoryResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(subCategories)
                        .build()
        );
    }

    @GetMapping("/{categoryId}/subcategories/hierarchy")
    public ResponseEntity<ApiResponse<List<SubCategoryResponse>>> getSubCategoryHierarchy(
            @PathVariable Integer categoryId) {
        List<SubCategoryResponse> hierarchy = categoryService.getSubCategoryHierarchy(categoryId);
        return ResponseEntity.ok(
                ApiResponse.<List<SubCategoryResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(hierarchy)
                        .build()
        );
    }

    @PostMapping("/subcategories/reorder")
    public ResponseEntity<ApiResponse<Void>> reorderSubCategories(
            @RequestParam(required = false) Integer parentId,
            @RequestBody List<Integer> subCategoryIds) {
        categoryService.reorderSubCategories(parentId, subCategoryIds);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Sắp xếp lại Category con thành công")
                        .build()
        );
    }
}