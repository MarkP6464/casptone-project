package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.CommentDto;
import com.example.capstoneproject.service.ReviewResponseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cv")
public class ReviewResponseController {

    @Autowired
    ReviewResponseService reviewResponseService;

    public ReviewResponseController(ReviewResponseService reviewResponseService) {
        this.reviewResponseService = reviewResponseService;
    }

    @PostMapping("/expert/{expertId}/review-response/{responseId}")
    public ResponseEntity<?> postReviewResponse(@PathVariable("expertId") Integer expertId, @PathVariable("responseId") Integer responseId, CommentDto dto) throws JsonProcessingException {
        return ResponseEntity.ok(reviewResponseService.createComment(expertId, responseId,dto));
    }

    @PutMapping("/expert/{expertId}/review-response/{responseId}")
    public ResponseEntity<?> putReviewResponse(@PathVariable("expertId") Integer expertId, @PathVariable("responseId") Integer responseId, String commentId,String newContent) throws JsonProcessingException {
        return ResponseEntity.ok(reviewResponseService.updateComment(expertId, responseId,commentId,newContent));
    }

    @DeleteMapping("/expert/{expertId}/review-response/{responseId}")
    public ResponseEntity<?> deleteReviewResponse(@PathVariable("expertId") Integer expertId, @PathVariable("responseId") Integer responseId, String commentId) throws JsonProcessingException {
        return ResponseEntity.ok(reviewResponseService.deleteComment(expertId, responseId,commentId));
    }
}
