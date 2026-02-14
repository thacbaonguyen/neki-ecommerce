package com.thacbao.neki.dto.request.product;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotNull(message = "ID khong được để trống")
    private Integer productId;

    @NotNull(message = "Rating không được để trống")
    @Min(value = 1, message = "Rating phải lớn hơn 1")
    @Max(value = 5, message = "Rating phải nhỏ hơn 5")
    private Integer rating;

    @NotBlank(message = "Tiêu đề không đươc để trống")
    private String title;
    @NotBlank(message = "Nội dung k được để trống")
    private String comment;
}
