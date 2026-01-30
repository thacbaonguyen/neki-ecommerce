package com.thacbao.neki.dto.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.thacbao.neki.model.Color;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ColorResponse {
    private Integer id;
    private String name;
    private String hexCode;

    public static ColorResponse from(Color color) {
        return ColorResponse.builder()
                .id(color.getId())
                .name(color.getName())
                .hexCode(color.getHexCode())
                .build();
    }
}