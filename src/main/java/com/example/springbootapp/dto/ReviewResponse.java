package com.example.springbootapp.dto;

import com.example.springbootapp.model.ModerationStatus;
import com.example.springbootapp.model.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private Integer rating;
    private ModerationStatus moderation;
    private Integer productId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    public static ReviewResponse fromEntity(Review review) {
        return new ReviewResponse(
            review.getId(),
            review.getTitle(),
            review.getContent(),
            review.getUserId(),
            review.getRating(),
            review.getModeration(),
            review.getProductId(),
            review.getCreatedAt(),
            review.getUpdatedAt()
        );
    }
}

