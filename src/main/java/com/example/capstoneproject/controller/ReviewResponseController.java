package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.CommentDto;
import com.example.capstoneproject.Dto.ReviewResponseDto;
import com.example.capstoneproject.Dto.ReviewResponseUpdateDto;
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
    @PreAuthorize("hasRole('ROLE_EXPERT')")
    public ResponseEntity<?> postReviewResponse(@PathVariable("expert-id") Integer expertId, @PathVariable("response-id") Integer responseId, CommentDto dto) throws JsonProcessingException {
        return ResponseEntity.ok(reviewResponseService.createComment(expertId, responseId,dto));
    }

    @PutMapping("/expert/{expert-id}/review-response/{response-id}/comment/{commentId}")
    @PreAuthorize("hasRole('ROLE_EXPERT')")
    public ResponseEntity<?> putReviewResponse(@PathVariable("expert-id") Integer expertId, @PathVariable("response-id") Integer responseId, @PathVariable("commentId") String commentId,String newContent) throws JsonProcessingException {
        return ResponseEntity.ok(reviewResponseService.updateComment(expertId, responseId,commentId,newContent));
    }

    @DeleteMapping("/expert/{expert-id}/review-response/{response-id}/comment/{comment-id}")
    @PreAuthorize("hasRole('ROLE_EXPERT')")
    public ResponseEntity<?> deleteReviewResponse(@PathVariable("expert-id") Integer expertId, @PathVariable("response-id") Integer responseId, @PathVariable("comment-id") String commentId) throws JsonProcessingException {
        return ResponseEntity.ok(reviewResponseService.deleteComment(expertId, responseId,commentId));
    }

    @PutMapping("/expert/{expert-id}/review-response/{response-id}/overall")
    @PreAuthorize("hasRole('ROLE_EXPERT')")
    public ResponseEntity<?> putReviewResponseOverall(@PathVariable("expert-id") Integer expertId, @PathVariable("response-id") Integer responseId, ReviewResponseUpdateDto dto)  {
        return ResponseEntity.ok(reviewResponseService.updateReviewResponse(expertId, responseId,dto));
    }

    @PutMapping("/expert/{expert-id}/review-response/{response-id}/public")
    @PreAuthorize("hasRole('ROLE_EXPERT')")
    public ResponseEntity<?> publicReviewResponseOverall(@PathVariable("expert-id") Integer expertId, @PathVariable("response-id") Integer responseId)  {
        return ResponseEntity.ok(reviewResponseService.publicReviewResponse(expertId, responseId));
    }

    @GetMapping("/user/{user-id}/review-request/{request-id}/review-response")
    @PreAuthorize("hasRole('ROLE_CANDIDATE')")
    public ResponseEntity<?> getReviewResponse(@PathVariable("user-id") Integer userId, @PathVariable("request-id") Integer requestId) throws JsonProcessingException {
        return ResponseEntity.ok(reviewResponseService.receiveReviewResponse(userId, requestId));
    }

    @GetMapping("/expert/{expert-id}/review-response/{response-id}")
    @PreAuthorize("hasRole('ROLE_EXPERT')")
    public ResponseEntity<?> getReviewResponseDetail(@PathVariable("expert-id") Integer expertId, @PathVariable("response-id") Integer responseId) throws JsonProcessingException {
        return ResponseEntity.ok(reviewResponseService.getReviewResponse(expertId, responseId));
    }

    @GetMapping("/expert/{expert-id}/review-responses")
    @PreAuthorize("hasRole('ROLE_EXPERT')")
    public ResponseEntity<List<ReviewResponseDto>> getDaftReviewResponses(
            @PathVariable("expert-id") Integer expertId,
            @RequestParam(name = "status", required = false) SendControl status
    ) throws JsonProcessingException {
        List<ReviewResponseDto> daftReviewResponses;

        if (status != null) {
            switch (status) {
                case DRAFT:
                    daftReviewResponses = reviewResponseService.daftReviewResponse(expertId, ReviewStatus.DRAFT);
                    break;
                case SEND:
                    daftReviewResponses = reviewResponseService.daftReviewResponse(expertId, ReviewStatus.SEND);
                    break;
                default:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        } else {
            daftReviewResponses = reviewResponseService.daftReviewResponse(expertId, null);
        }

        if (daftReviewResponses != null && !daftReviewResponses.isEmpty()) {
            return ResponseEntity.ok(daftReviewResponses);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

}
