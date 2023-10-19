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

    @PutMapping("/{userId}/rating/{ratingId}")
    public ResponseEntity<?> updateRating(@PathVariable("userId") Integer userId, @PathVariable("ratingId") Integer ratingId, ReviewRatingDto dto) {
        if (reviewRatingService.updateReviewRating(userId, ratingId, dto)) {
            return ResponseEntity.ok("Update success");
        } else {
            return ResponseEntity.badRequest().body("Update failed");
        }
    }

    @DeleteMapping("/{userId}/rating/{ratingId}")
    public ResponseEntity<?> deleteRating(@PathVariable("userId") Integer userId, @PathVariable("ratingId") Integer ratingId) {
        if (reviewRatingService.deleteReviewRating(userId, ratingId)) {
            return ResponseEntity.ok("Delete success");
        } else {
            return ResponseEntity.badRequest().body("Delete failed");
        }
    }

    @PostMapping("/{userId}/review-response/{responseId}")
    public ResponseEntity<?> createRating(@PathVariable("userId") Integer userId, @PathVariable("responseId") Integer responseId, ReviewRatingDto dto) {
        return ResponseEntity.ok(reviewRatingService.createReviewRating(userId, responseId, dto));
    }
}
