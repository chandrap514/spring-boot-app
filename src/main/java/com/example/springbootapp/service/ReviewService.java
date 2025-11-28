package com.example.springbootapp.service;

import com.example.springbootapp.dto.CreateReviewRequest;
import com.example.springbootapp.dto.ReviewResponse;
import com.example.springbootapp.dto.UpdateReviewRequest;
import com.example.springbootapp.model.ModerationStatus;
import com.example.springbootapp.model.Review;
import com.example.springbootapp.repository.ReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    
    private static final Logger logger = LoggerFactory.getLogger(ReviewService.class);
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    /**
     * Get reviews by product ID with pagination
     */
    public List<ReviewResponse> getReviewsByProductId(Integer productId, int page, int limit) {
        logger.debug("Fetching reviews for productId: {}, page: {}, limit: {}", productId, page, limit);
        
        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Page<Review> reviewPage = reviewRepository.findByProductId(productId, pageable);
        
        return reviewPage.getContent().stream()
                .map(ReviewResponse::fromEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * Create a new review
     */
    public ReviewResponse createReview(Long userId, CreateReviewRequest request) {
        logger.debug("Creating review for userId: {}, productId: {}", userId, request.getProductId());
        
        Review review = new Review();
        review.setTitle(request.getTitle());
        review.setContent(request.getContent());
        review.setRating(request.getRating());
        review.setProductId(request.getProductId());
        review.setUserId(userId);
        review.setModeration(ModerationStatus.POSTED);
        
        Review savedReview = reviewRepository.save(review);
        logger.info("Review created successfully with id: {}", savedReview.getId());
        
        return ReviewResponse.fromEntity(savedReview);
    }
    
    /**
     * Update an existing review (supports partial updates)
     */
    public ReviewResponse updateReview(Long userId, Long reviewId, UpdateReviewRequest request) {
        logger.debug("Updating review id: {} for userId: {}", reviewId, userId);
        
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewId));
        
        // Check if the user is the owner of the review
        if (!review.getUserId().equals(userId)) {
            throw new IllegalArgumentException("User is not authorized to update this review");
        }
        
        // Only update fields that are present in the request
        if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
            review.setTitle(request.getTitle());
        }
        
        if (request.getContent() != null && !request.getContent().trim().isEmpty()) {
            review.setContent(request.getContent());
        }
        
        if (request.getRating() != null) {
            review.setRating(request.getRating());
        }
        
        Review updatedReview = reviewRepository.save(review);
        logger.info("Review updated successfully with id: {}", updatedReview.getId());
        
        return ReviewResponse.fromEntity(updatedReview);
    }
    
    /**
     * Delete a review
     */
    public void deleteReview(Long userId, Long reviewId) {
        logger.debug("Deleting review id: {} for userId: {}", reviewId, userId);
        
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Review not found with id: " + reviewId));
        
        // Check if the user is the owner of the review
        if (!review.getUserId().equals(userId)) {
            throw new IllegalArgumentException("User is not authorized to delete this review");
        }
        
        reviewRepository.delete(review);
        logger.info("Review deleted successfully with id: {}", reviewId);
    }
}

