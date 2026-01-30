package com.thacbao.neki.repositories;

import com.thacbao.neki.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

// Elasticsearch Repository for full-text search
@Repository
interface ProductElasticsearchRepository extends ElasticsearchRepository<Product, Integer> {

    Page<Product> findByNameContainingOrDescriptionContaining(String name, String description, Pageable pageable);

    @Query("{\"bool\": {\"must\": [{\"multi_match\": {\"query\": \"?0\", \"fields\": [\"name^3\", \"description\"]}}], \"filter\": [{\"term\": {\"isActive\": true}}]}}")
    Page<Product> searchProducts(String keyword, Pageable pageable);
}