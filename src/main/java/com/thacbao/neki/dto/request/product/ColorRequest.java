package com.thacbao.neki.dto.request.product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class ColorRequest {
    @NotBlank(message = "Tên màu không được để trống")
    @Size(max = 50, message = "Tên màu tối đa 50 ký tự")
    private String name;

    @NotBlank(message = "Mã màu hex không được để trống")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Mã màu hex không hợp lệ (ví dụ: #FF5733)")
    private String hexCode;
}