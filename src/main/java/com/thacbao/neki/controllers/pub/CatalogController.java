package com.thacbao.neki.controllers.pub;

import com.thacbao.neki.dto.response.*;
import com.thacbao.neki.services.AttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog")
@RequiredArgsConstructor
class CatalogController {

    private final AttributeService attributeService;

    @GetMapping("/brands")
    public ResponseEntity<ApiResponse<List<BrandResponse>>> getAllBrands() {
        List<BrandResponse> brands = attributeService.getAllActiveBrands();
        return ResponseEntity.ok(
                ApiResponse.<List<BrandResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(brands)
                        .build()
        );
    }

    @GetMapping("/collections")
    public ResponseEntity<ApiResponse<List<CollectionResponse>>> getAllCollections() {
        List<CollectionResponse> collections = attributeService.getAllActiveCollections();
        return ResponseEntity.ok(
                ApiResponse.<List<CollectionResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(collections)
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

    @GetMapping("/collections/slug/{slug}")
    public ResponseEntity<ApiResponse<CollectionResponse>> getCollectionBySlug(@PathVariable String slug) {
        CollectionResponse collection = attributeService.getCollectionBySlug(slug);
        return ResponseEntity.ok(
                ApiResponse.<CollectionResponse>builder()
                        .code(200)
                        .status("success")
                        .data(collection)
                        .build()
        );
    }

    @GetMapping("/topics")
    public ResponseEntity<ApiResponse<List<TopicResponse>>> getAllTopics() {
        List<TopicResponse> topics = attributeService.getAllActiveTopics();
        return ResponseEntity.ok(
                ApiResponse.<List<TopicResponse>>builder()
                        .code(200)
                        .status("success")
                        .data(topics)
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

    @GetMapping("/topics/slug/{slug}")
    public ResponseEntity<ApiResponse<TopicResponse>> getTopicBySlug(@PathVariable String slug) {
        TopicResponse topic = attributeService.getTopicBySlug(slug);
        return ResponseEntity.ok(
                ApiResponse.<TopicResponse>builder()
                        .code(200)
                        .status("success")
                        .data(topic)
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