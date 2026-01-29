package com.thacbao.neki.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    private int code;
    private String status;
    private String message;
    private T data;
    private Object errors; // For validation errors
}