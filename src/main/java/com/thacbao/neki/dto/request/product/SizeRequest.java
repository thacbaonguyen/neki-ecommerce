package com.thacbao.neki.dto.request.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class SizeRequest {
    @NotBlank(message = "Tên size không được để trống")
    @Size(max = 20, message = "Tên size tối đa 20 ký tự")
    private String name;

    @NotBlank(message = "Loại danh mục không được để trống")
    @Size(max = 50, message = "Loại danh mục tối đa 50 ký tự")
    private String categoryType;

    @Min(value = 0, message = "Thứ tự hiển thị phải >= 0")
    private Integer displayOrder;
}