package com.thacbao.neki.dto.request.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotBlank(message = "Phone không được để trống")
    @Size(max = 15, message = "SĐT không được quá 15 ký tự")
    private String phoneDelivery;

    @NotBlank(message = "Tên tỉnh/TP khong được để trống")
    @Size(max = 100, message = "Tên tỉnh/tp không được quá 100 ký tự")
    private String province;

    @NotBlank(message = "Tên quận không được để trống")
    @Size(max = 100, message = "Tên quận tối đa 100 ký tự")
    private String district;

    @NotBlank(message = "Phường không được để trống")
    @Size(max = 100, message = "Phường tối đa 100 ký tự")
    private String ward;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 300, message = "Địa chỉ chi tiết tối đa 300 ký tự")
    private String addressDetail;

    @Size(max = 2000, message = "Note tối đa 2000 ký tự")
    private String note;

    @NotNull(message = "Phương thức thanh toán không được để trống")
    private Integer paymentMethodId;

    @Size(max = 100, message = "Discount tối đa 100 ký tự")
    private String discountCode;
}
