package com.thacbao.neki.dto.request.product;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionRequest {
    @NotBlank(message = "Tên bộ sưu tập không được để trống")
    @Size(max = 100, message = "Tên bộ sưu tập tối đa 100 ký tự")
    private String name;

    @Size(max = 500, message = "Mô tả tối đa 500 ký tự")
    private String description;

    private Boolean isActive;

    private Set<Integer> subCategoryIds;
}