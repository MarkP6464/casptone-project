package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.ReviewRequestAddDto;
import com.example.capstoneproject.Dto.ReviewRequestDto;
import com.example.capstoneproject.enums.AcceptControl;
import com.example.capstoneproject.enums.ReviewStatus;
import com.example.capstoneproject.service.ReviewRequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/expert/{expert-id}/request-reviews")
    @PreAuthorize("hasRole('ROLE_EXPERT')")
    public ResponseEntity<?> getAllReviewRequest(@PathVariable("expert-id") Integer expertId, @RequestParam("theRelation") AcceptControl theRelation, @RequestParam("orderByDate") String orderByDate) {
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


    @PostMapping("/{cv-id}/expert/{expert-id}/request-review")
    @PreAuthorize("hasRole('ROLE_CANDIDATE')")
    public ReviewRequestDto postReviewRequest(@PathVariable("cv-id") int cvId, @PathVariable("expert-id") int expertId, @RequestBody ReviewRequestAddDto Dto) {
        return reviewRequestService.createReviewRequest(cvId,expertId,Dto);
    }

    @PutMapping("/expert/{expert-id}/review-request/{request-id}")
    @PreAuthorize("hasRole('ROLE_EXPERT')")
    public ResponseEntity<?> updateEducation(@PathVariable("expert-id") Integer expertId, @PathVariable("request-id") Integer requestId, AcceptControl theRelation) throws JsonProcessingException {
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
