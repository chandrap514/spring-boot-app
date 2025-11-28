package com.example.springbootapp.controller;

import com.example.springbootapp.dto.CreateReviewRequest;
import com.example.springbootapp.dto.ReviewResponse;
import com.example.springbootapp.dto.UpdateReviewRequest;
import com.example.springbootapp.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    
    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);
    
    @Autowired
    private ReviewService reviewService;
    
    /**
     * GET /api/reviews?productId={id}&page={pageNo}&limit={limit}
     * Headers: userId
     */
    @GetMapping
    public ResponseEntity<?> getReviews(
            @RequestParam Integer productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestHeader(value = "userId", required = false) Long userId) {
        
        try {
            logger.debug("GET /api/reviews - productId: {}, page: {}, limit: {}, userId: {}", 
                    productId, page, limit, userId);
            
            List<ReviewResponse> reviews = reviewService.getReviewsByProductId(productId, page, limit);
            return ResponseEntity.ok(reviews);
            
        } catch (Exception e) {
            logger.error("Error fetching reviews", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Failed to fetch reviews: " + e.getMessage()));
        }
    }
    
    /**
     * POST /api/reviews
     * Headers: userId
     * Body: {title, content, rating, productId}
     */
    @PostMapping
    public ResponseEntity<?> createReview(
            @RequestHeader("userId") Long userId,
            @Valid @RequestBody CreateReviewRequest request) {
        
        try {
            logger.debug("POST /api/reviews - userId: {}, request: {}", userId, request);
            
            ReviewResponse review = reviewService.createReview(userId, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(review);
            
        } catch (Exception e) {
            logger.error("Error creating review", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Failed to create review: " + e.getMessage()));
        }
    }
    
    /**
     * PUT /api/reviews/{id}
     * Headers: userId
     * Body: {title, content, rating}
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(
            @PathVariable Long id,
            @RequestHeader("userId") Long userId,
            @Valid @RequestBody UpdateReviewRequest request) {
        
        try {
            logger.debug("PUT /api/reviews/{} - userId: {}, request: {}", id, userId, request);
            
            ReviewResponse review = reviewService.updateReview(userId, id, request);
            return ResponseEntity.ok(review);
            
        } catch (EntityNotFoundException e) {
            logger.error("Review not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
                    
        } catch (IllegalArgumentException e) {
            logger.error("Authorization error", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(createErrorResponse(e.getMessage()));
                    
        } catch (Exception e) {
            logger.error("Error updating review", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Failed to update review: " + e.getMessage()));
        }
    }
    
    /**
     * DELETE /api/reviews/{id}
     * Headers: userId
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long id,
            @RequestHeader("userId") Long userId) {
        
        try {
            logger.debug("DELETE /api/reviews/{} - userId: {}", id, userId);
            
            reviewService.deleteReview(userId, id);
            return ResponseEntity.ok(createSuccessResponse("Review deleted successfully"));
            
        } catch (EntityNotFoundException e) {
            logger.error("Review not found", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse(e.getMessage()));
                    
        } catch (IllegalArgumentException e) {
            logger.error("Authorization error", e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(createErrorResponse(e.getMessage()));
                    
        } catch (Exception e) {
            logger.error("Error deleting review", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse("Failed to delete review: " + e.getMessage()));
        }
    }
    
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("error", message);
        return response;
    }
    
    private Map<String, String> createSuccessResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        return response;
    }
}

