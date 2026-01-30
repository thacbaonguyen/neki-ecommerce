package com.thacbao.neki.dto.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thacbao.neki.model.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SizeResponse {
    private Integer id;
    private String name;
    private String categoryType;
    private Integer displayOrder;

    public static SizeResponse from(Size size) {
        return SizeResponse.builder()
                .id(size.getId())
                .name(size.getName())
                .categoryType(size.getCategoryType())
                .displayOrder(size.getDisplayOrder())
                .build();
    }
}
