package com.thacbao.neki.dto.response;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterOptionsResponse {
    private List<CategoryResponse> categories;
    private List<BrandResponse> brands;
    private List<CollectionResponse> collections;
    private List<TopicResponse> topics;
    private List<ColorResponse> colors;
    private List<SizeResponse> sizes;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
}
