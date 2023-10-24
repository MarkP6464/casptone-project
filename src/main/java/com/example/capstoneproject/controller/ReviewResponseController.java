package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.CommentDto;
import com.example.capstoneproject.Dto.ReviewResponseDto;
import com.example.capstoneproject.Dto.ReviewResponseUpdateDto;
import com.example.capstoneproject.enums.ReviewStatus;
import com.example.capstoneproject.service.ReviewResponseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/expert/{expertId}/review-response/{responseId}/comment")
    public ResponseEntity<?> postReviewResponse(@PathVariable("expertId") Integer expertId, @PathVariable("responseId") Integer responseId, CommentDto dto) throws JsonProcessingException {
        return ResponseEntity.ok(reviewResponseService.createComment(expertId, responseId,dto));
    }

    @PutMapping("/expert/{expertId}/review-response/{responseId}/comment/{commentId}")
    public ResponseEntity<?> putReviewResponse(@PathVariable("expertId") Integer expertId, @PathVariable("responseId") Integer responseId, @PathVariable("commentId") String commentId,String newContent) throws JsonProcessingException {
        return ResponseEntity.ok(reviewResponseService.updateComment(expertId, responseId,commentId,newContent));
    }

    @DeleteMapping("/expert/{expertId}/review-response/{responseId}/comment/{commentId}")
    public ResponseEntity<?> deleteReviewResponse(@PathVariable("expertId") Integer expertId, @PathVariable("responseId") Integer responseId, @PathVariable("commentId") String commentId) throws JsonProcessingException {
        return ResponseEntity.ok(reviewResponseService.deleteComment(expertId, responseId,commentId));
    }

    @PutMapping("/expert/{expertId}/review-response/{responseId}/overall")
    public ResponseEntity<?> putReviewResponseOverall(@PathVariable("expertId") Integer expertId, @PathVariable("responseId") Integer responseId, ReviewResponseUpdateDto dto)  {
        return ResponseEntity.ok(reviewResponseService.updateReviewResponse(expertId, responseId,dto));
    }

    @PutMapping("/expert/{expertId}/review-response/{responseId}/public")
    public ResponseEntity<?> publicReviewResponseOverall(@PathVariable("expertId") Integer expertId, @PathVariable("responseId") Integer responseId)  {
        return ResponseEntity.ok(reviewResponseService.publicReviewResponse(expertId, responseId));
    }

    @GetMapping("/user/{userId}/review-request/{requestId}/review-response")
    public ResponseEntity<?> getReviewResponse(@PathVariable("userId") Integer userId, @PathVariable("requestId") Integer requestId) throws JsonProcessingException {
        return ResponseEntity.ok(reviewResponseService.receiveReviewResponse(userId, requestId));
    }

    @GetMapping("/expert/{expertId}/review-response/{responseId}")
    public ResponseEntity<?> getReviewResponseDetail(@PathVariable("expertId") Integer expertId, @PathVariable("responseId") Integer responseId) throws JsonProcessingException {
        return ResponseEntity.ok(reviewResponseService.getReviewResponse(expertId, responseId));
    }

    @GetMapping("/expert/{expertId}/review-responses")
    public ResponseEntity<List<ReviewResponseDto>> getDaftReviewResponses(
            @PathVariable("expertId") Integer expertId,
            @RequestParam(name = "status", required = false) String status
    ) throws JsonProcessingException {
        List<ReviewResponseDto> daftReviewResponses;

        if (status != null) {
            switch (status) {
                case "draft":
                    daftReviewResponses = reviewResponseService.daftReviewResponse(expertId, ReviewStatus.DRAFT);
                    break;
                case "send":
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