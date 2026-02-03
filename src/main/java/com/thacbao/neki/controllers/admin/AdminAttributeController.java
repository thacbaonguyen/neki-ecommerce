package com.thacbao.neki.controllers.admin;

import com.thacbao.neki.dto.request.product.*;
import com.thacbao.neki.dto.response.*;
import com.thacbao.neki.services.AttributeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminAttributeController {

    private final AttributeService attributeService;

    // Brand
    @PostMapping("/brands")
    public ResponseEntity<ApiResponse<BrandResponse>> createBrand(
            @Valid @RequestBody BrandRequest request) {
        BrandResponse brand = attributeService.createBrand(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<BrandResponse>builder()
                        .code(201)
                        .status("success")
                        .message("Tạo thương hiệu thành công")
                        .data(brand)
                        .build()
        );
    }

    @PutMapping("/brands/{id}")
    public ResponseEntity<ApiResponse<BrandResponse>> updateBrand(
            @PathVariable Integer id,
            @Valid @RequestBody BrandRequest request) {
        BrandResponse brand = attributeService.updateBrand(id, request);
        return ResponseEntity.ok(
                ApiResponse.<BrandResponse>builder()
                        .code(200)
                        .status("success")
                        .message("Cập nhật thương hiệu thành công")
                        .data(brand)
                        .build()
        );
    }

    @DeleteMapping("/brands/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBrand(@PathVariable Integer id) {
        attributeService.deleteBrand(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Xóa thương hiệu thành công")
                        .build()
        );
    }

    @GetMapping("/brands/{id}")
    public ResponseEntity<ApiResponse<BrandResponse>> getBrandById(@PathVariable Integer id) {
        BrandResponse brand = attributeService.getBrandById(id);
        return ResponseEntity.ok(
                ApiResponse.<BrandResponse>builder()
                        .code(200)
                        .status("success")
                        .data(brand)
                        .build()
        );
    }

    @GetMapping("/brands")
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getAllBrands() {
        List<BrandResponse> brands = attributeService.getAllBrands();
        return ResponseEntity.ok(
                ApiResponse.<List<BrandResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(brands)
                        .build()
        );
    }

    // Collection
    @PostMapping("/collections")
    public ResponseEntity<ApiResponse<CollectionResponse>> createCollection(
            @Valid @RequestBody CollectionRequest request) {
        CollectionResponse collection = attributeService.createCollection(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<CollectionResponse>builder()
                        .code(201)
                        .status("success")
                        .message("Tạo bộ sưu tập thành công")
                        .data(collection)
                        .build()
        );
    }

    @PutMapping("/collections/{id}")
    public ResponseEntity<ApiResponse<CollectionResponse>> updateCollection(
            @PathVariable Integer id,
            @Valid @RequestBody CollectionRequest request) {
        CollectionResponse collection = attributeService.updateCollection(id, request);
        return ResponseEntity.ok(
                ApiResponse.<CollectionResponse>builder()
                        .code(200)
                        .status("success")
                        .message("Cập nhật bộ sưu tập thành công")
                        .data(collection)
                        .build()
        );
    }

    @DeleteMapping("/collections/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCollection(@PathVariable Integer id) {
        attributeService.deleteCollection(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Xóa bộ sưu tập thành công")
                        .build()
        );
    }

    @GetMapping("/collections/{id}")
    public ResponseEntity<ApiResponse<CollectionResponse>> getCollectionById(@PathVariable Integer id) {
        CollectionResponse collection = attributeService.getCollectionById(id);
        return ResponseEntity.ok(
                ApiResponse.<CollectionResponse>builder()
                        .code(200)
                        .status("success")
                        .data(collection)
                        .build()
        );
    }

    @GetMapping("/collections")
    public ResponseEntity<ApiResponse<List<CollectionResponse>>> getAllCollections() {
        List<CollectionResponse> collections = attributeService.getAllCollections();
        return ResponseEntity.ok(
                ApiResponse.<List<CollectionResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(collections)
                        .build()
        );
    }

    //  Topic Endpoints 

    @PostMapping("/topics")
    public ResponseEntity<ApiResponse<TopicResponse>> createTopic(
            @Valid @RequestBody TopicRequest request) {
        TopicResponse topic = attributeService.createTopic(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<TopicResponse>builder()
                        .code(201)
                        .status("success")
                        .message("Tạo chủ đề thành công")
                        .data(topic)
                        .build()
        );
    }

    @PutMapping("/topics/{id}")
    public ResponseEntity<ApiResponse<TopicResponse>> updateTopic(
            @PathVariable Integer id,
            @Valid @RequestBody TopicRequest request) {
        TopicResponse topic = attributeService.updateTopic(id, request);
        return ResponseEntity.ok(
                ApiResponse.<TopicResponse>builder()
                        .code(200)
                        .status("success")
                        .message("Cập nhật chủ đề thành công")
                        .data(topic)
                        .build()
        );
    }

    @DeleteMapping("/topics/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTopic(@PathVariable Integer id) {
        attributeService.deleteTopic(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Xóa chủ đề thành công")
                        .build()
        );
    }

    @GetMapping("/topics/{id}")
    public ResponseEntity<ApiResponse<TopicResponse>> getTopicById(@PathVariable Integer id) {
        TopicResponse topic = attributeService.getTopicById(id);
        return ResponseEntity.ok(
                ApiResponse.<TopicResponse>builder()
                        .code(200)
                        .status("success")
                        .data(topic)
                        .build()
        );
    }

    @GetMapping("/topics")
    public ResponseEntity<ApiResponse<List<TopicResponse>>> getAllTopics() {
        List<TopicResponse> topics = attributeService.getAllTopics();
        return ResponseEntity.ok(
                ApiResponse.<List<TopicResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(topics)
                        .build()
        );
    }

    //  Color
    @PostMapping("/colors")
    public ResponseEntity<ApiResponse<ColorResponse>> createColor(
            @Valid @RequestBody ColorRequest request) {
        ColorResponse color = attributeService.createColor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<ColorResponse>builder()
                        .code(201)
                        .status("success")
                        .message("Tạo màu sắc thành công")
                        .data(color)
                        .build()
        );
    }

    @PutMapping("/colors/{id}")
    public ResponseEntity<ApiResponse<ColorResponse>> updateColor(
            @PathVariable Integer id,
            @Valid @RequestBody ColorRequest request) {
        ColorResponse color = attributeService.updateColor(id, request);
        return ResponseEntity.ok(
                ApiResponse.<ColorResponse>builder()
                        .code(200)
                        .status("success")
                        .message("Cập nhật màu sắc thành công")
                        .data(color)
                        .build()
        );
    }

    @DeleteMapping("/colors/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteColor(@PathVariable Integer id) {
        attributeService.deleteColor(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Xóa màu sắc thành công")
                        .build()
        );
    }

    @GetMapping("/colors/{id}")
    public ResponseEntity<ApiResponse<ColorResponse>> getColorById(@PathVariable Integer id) {
        ColorResponse color = attributeService.getColorById(id);
        return ResponseEntity.ok(
                ApiResponse.<ColorResponse>builder()
                        .code(200)
                        .status("success")
                        .data(color)
                        .build()
        );
    }

    @GetMapping("/colors")
    public ResponseEntity<ApiResponse<List<ColorResponse>>> getAllColors() {
        List<ColorResponse> colors = attributeService.getAllColors();
        return ResponseEntity.ok(
                ApiResponse.<List<ColorResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(colors)
                        .build()
        );
    }

    //  Size Endpoints 

    @PostMapping("/sizes")
    public ResponseEntity<ApiResponse<SizeResponse>> createSize(
            @Valid @RequestBody SizeRequest request) {
        SizeResponse size = attributeService.createSize(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<SizeResponse>builder()
                        .code(201)
                        .status("success")
                        .message("Tạo size thành công")
                        .data(size)
                        .build()
        );
    }

    @PutMapping("/sizes/{id}")
    public ResponseEntity<ApiResponse<SizeResponse>> updateSize(
            @PathVariable Integer id,
            @Valid @RequestBody SizeRequest request) {
        SizeResponse size = attributeService.updateSize(id, request);
        return ResponseEntity.ok(
                ApiResponse.<SizeResponse>builder()
                        .code(200)
                        .status("success")
                        .message("Cập nhật size thành công")
                        .data(size)
                        .build()
        );
    }

    @DeleteMapping("/sizes/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSize(@PathVariable Integer id) {
        attributeService.deleteSize(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .code(200)
                        .status("success")
                        .message("Xóa size thành công")
                        .build()
        );
    }

    @GetMapping("/sizes/{id}")
    public ResponseEntity<ApiResponse<SizeResponse>> getSizeById(@PathVariable Integer id) {
        SizeResponse size = attributeService.getSizeById(id);
        return ResponseEntity.ok(
                ApiResponse.<SizeResponse>builder()
                        .code(200)
                        .status("success")
                        .data(size)
                        .build()
        );
    }

    @GetMapping("/sizes")
    public ResponseEntity<ApiResponse<List<SizeResponse>>> getSizes(
            @RequestParam(required = false) String type) {
        List<SizeResponse> sizes = type != null
                ? attributeService.getSizesByCategoryType(type)
                : attributeService.getAllSizes();
        return ResponseEntity.ok(
                ApiResponse.<List<SizeResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(sizes)
                        .build()
        );
    }
}