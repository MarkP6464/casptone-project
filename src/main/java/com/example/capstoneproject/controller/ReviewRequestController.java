package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.ReviewRequestAddDto;
import com.example.capstoneproject.Dto.ReviewRequestDto;
import com.example.capstoneproject.enums.AcceptControl;
import com.example.capstoneproject.enums.ReviewStatus;
import com.example.capstoneproject.service.ReviewRequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cv")
public class ReviewRequestController {
    @Autowired
    ReviewRequestService reviewRequestService;

    public ReviewRequestController(ReviewRequestService reviewRequestService) {
        this.reviewRequestService = reviewRequestService;
    }

    @GetMapping("/expert/{expertId}/request-reviews")
    public ResponseEntity<?> getAllReviewRequest(@PathVariable("expertId") Integer expertId, @RequestParam("theRelation") AcceptControl theRelation, @RequestParam("orderByDate") String orderByDate) {
        List<ReviewRequestDto> result;
        switch (theRelation) {
            case ACCEPT:
                result = reviewRequestService.getAllReviewRequest(expertId, ReviewStatus.ACCEPT, orderByDate);
                break;
            case REJECT:
                result = reviewRequestService.getAllReviewRequest(expertId, ReviewStatus.REJECT, orderByDate);
                break;
            case PROCESS:
                result = reviewRequestService.getAllReviewRequest(expertId, ReviewStatus.PROCESSING, orderByDate);
                break;
            default:
                return ResponseEntity.badRequest().body("Invalid request!!");
        }
        return ResponseEntity.ok(result);
    }


    @PostMapping("/{cvId}/expert/{expertId}/request-review")
    public ReviewRequestDto postReviewRequest(@PathVariable("cvId") int cvId, @PathVariable("expertId") int expertId, @RequestBody ReviewRequestAddDto Dto) {
        return reviewRequestService.createReviewRequest(cvId,expertId,Dto);
    }

    @PutMapping("/expert/{expertId}/review-request/{requestId}")
    public ResponseEntity<?> updateEducation(@PathVariable("expertId") Integer expertId, @PathVariable("requestId") Integer requestId, AcceptControl theRelation) throws JsonProcessingException {
        switch (theRelation) {
            case ACCEPT:
                return ResponseEntity.ok(reviewRequestService.acceptReviewRequest(expertId, requestId));
            case REJECT:
                return ResponseEntity.ok(reviewRequestService.rejectReviewRequest(expertId, requestId));
            default:
                return ResponseEntity.badRequest().body("Invalid request!!");
        }
    }

}
