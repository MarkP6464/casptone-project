package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.enums.ReviewStatus;
import com.example.capstoneproject.enums.SendControl;
import com.example.capstoneproject.service.ReviewResponseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cv")
public class ReviewResponseController {

    @Autowired
    ReviewResponseService reviewResponseService;

    public ReviewResponseController(ReviewResponseService reviewResponseService) {
        this.reviewResponseService = reviewResponseService;
    }

    @PostMapping("/expert/{expert-id}/review-response/{response-id}/comment")
    @PreAuthorize("hasAuthority('create:expert')")
    public ResponseEntity<?> postReviewResponse(@PathVariable("expert-id") Integer expertId, @PathVariable("response-id") Integer responseId, @RequestBody CommentDto dto) throws JsonProcessingException {
        return ResponseEntity.ok(reviewResponseService.createComment(expertId, responseId,dto));
    }

    @PostMapping("/user/review-response/{response-id}/comment/rating")
    @PreAuthorize("hasAnyAuthority('create:candidate','create:expert')")
    public ResponseEntity<?> postReviewResponseRating(@PathVariable("response-id") Integer responseId, @RequestBody ReviewRatingAddDto dto) {
        return ResponseEntity.ok(reviewResponseService.sendReviewRating(responseId,dto));
    }

    @PutMapping("/expert/{expert-id}/review-response/{response-id}/comment/{comment-id}")
    @PreAuthorize("hasAuthority('update:expert')")
    public ResponseEntity<?> putReviewResponse(
            @PathVariable("expert-id") Integer expertId,
            @PathVariable("response-id") Integer responseId,
            @PathVariable("comment-id") String commentId,
            @RequestBody CommentNewDto dto
    ) throws JsonProcessingException {
        return ResponseEntity.ok(reviewResponseService.updateComment(expertId, responseId, commentId, dto));
    }

    @DeleteMapping("/expert/{expert-id}/review-response/{response-id}/comment/{comment-id}")
    @PreAuthorize("hasAuthority('delete:expert')")
    public ResponseEntity<?> deleteReviewResponse(@PathVariable("expert-id") Integer expertId, @PathVariable("response-id") Integer responseId, @PathVariable("comment-id") String commentId) throws JsonProcessingException {
        return ResponseEntity.ok(reviewResponseService.deleteComment(expertId, responseId,commentId));
    }

    @PutMapping("/expert/{expert-id}/review-response/{response-id}/overall")
    @PreAuthorize("hasAuthority('update:expert')")
    public ResponseEntity<?> putReviewResponseOverall(@PathVariable("expert-id") Integer expertId, @PathVariable("response-id") Integer responseId, @RequestBody ReviewResponseUpdateDto dto) throws JsonProcessingException {
        if(reviewResponseService.updateReviewResponse(expertId, responseId,dto)){
            return ResponseEntity.ok("Save successful.");
        }
        return ResponseEntity.ok("Save fail.");

    }

    @PutMapping("/expert/{expert-id}/review-response/{response-id}/public")
    @PreAuthorize("hasAuthority('update:expert')")
    public ResponseEntity<?> publicReviewResponseOverall(@PathVariable("expert-id") Integer expertId, @PathVariable("response-id") Integer responseId)  {
        return ResponseEntity.ok(reviewResponseService.publicReviewResponse(expertId, responseId));
    }

    @GetMapping("/user/{user-id}/review-request/{request-id}/review-response")
    @PreAuthorize("hasAnyAuthority('read:candidate','read:expert')")
    public ResponseEntity<?> getReviewResponse(@PathVariable("user-id") Integer userId, @PathVariable("request-id") Integer requestId) throws JsonProcessingException {
        return ResponseEntity.ok(reviewResponseService.receiveReviewResponse(userId, requestId));
    }

    @GetMapping("/expert/{expert-id}/review-request/{request-id}/review-response")
    @PreAuthorize("hasAuthority('read:expert')")
    public ResponseEntity<?> getReviewResponseDetail(@PathVariable("expert-id") Integer expertId, @PathVariable("request-id") Integer requestId) throws JsonProcessingException {
        return ResponseEntity.ok(reviewResponseService.getReviewResponse(expertId, requestId));
    }

}
