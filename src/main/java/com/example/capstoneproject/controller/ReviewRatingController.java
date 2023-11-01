package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.ReviewRatingDto;
import com.example.capstoneproject.service.ReviewRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class ReviewRatingController {

    @Autowired
    ReviewRatingService reviewRatingService;

    public ReviewRatingController(ReviewRatingService reviewRatingService) {
        this.reviewRatingService = reviewRatingService;
    }

    @PutMapping("/{user-id}/rating/{rating-id}")
    public ResponseEntity<?> updateRating(@PathVariable("user-id") Integer userId, @PathVariable("rating-id") Integer ratingId, ReviewRatingDto dto) {
        if (reviewRatingService.updateReviewRating(userId, ratingId, dto)) {
            return ResponseEntity.ok("Update success");
        } else {
            return ResponseEntity.badRequest().body("Update failed");
        }
    }

    @DeleteMapping("/{user-id}/rating/{rating-id}")
    public ResponseEntity<?> deleteRating(@PathVariable("user-id") Integer userId, @PathVariable("rating-id") Integer ratingId) {
        if (reviewRatingService.deleteReviewRating(userId, ratingId)) {
            return ResponseEntity.ok("Delete success");
        } else {
            return ResponseEntity.badRequest().body("Delete failed");
        }
    }

    @PostMapping("/{user-id}/review-response/{review-response-id}")
    public ResponseEntity<?> createRating(@PathVariable("user-id") Integer userId, @PathVariable("review-response-id") Integer responseId, ReviewRatingDto dto) {
        return ResponseEntity.ok(reviewRatingService.createReviewRating(userId, responseId, dto));
    }
}
