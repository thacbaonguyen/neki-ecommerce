package com.thacbao.neki.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.thacbao.neki.model.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewResponse {

    private Integer id;
    private Integer rating;
    private String title;
    private String comment;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    private UserResponseDTO user;

    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .title(review.getTitle())
                .comment(review.getComment())
                .createAt(review.getCreatedAt())
                .updateAt(review.getUpdatedAt())
                .user(UserResponseDTO.from(review.getUser()))
                .build();
    }
}
