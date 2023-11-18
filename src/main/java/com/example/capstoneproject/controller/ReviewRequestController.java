package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.ReviewRequestAddDto;
import com.example.capstoneproject.Dto.ReviewRequestDto;
import com.example.capstoneproject.Dto.responses.ReviewRequestSecondViewDto;
import com.example.capstoneproject.enums.SortBy;
import com.example.capstoneproject.enums.SortOrder;
import com.example.capstoneproject.service.ReviewRequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PostMapping("/{cv-id}/expert/{expert-id}/request-review")
    @PreAuthorize("hasAnyAuthority('create:candidate','create:expert')")
    public ReviewRequestDto postReviewRequest(@PathVariable("cv-id") int cvId, @PathVariable("expert-id") int expertId, @RequestBody ReviewRequestAddDto Dto) throws JsonProcessingException {
        return reviewRequestService.createReviewRequest(cvId,expertId,Dto);
    }

    @GetMapping("/candidate/{candidate-id}/review-requests")
    @PreAuthorize("hasAnyAuthority('read:candidate','read:expert')")
    public List<ReviewRequestSecondViewDto> getListCandidateReviewRequest(
            @PathVariable("candidate-id") Integer candidateId,
            @RequestParam(required = false, defaultValue = "price") SortBy sortBy,
            @RequestParam(required = false, defaultValue = "asc") SortOrder sortOrder,
            @RequestParam(required = false) String searchTerm
    ) {
        return reviewRequestService.getListReviewRequestCandidate(candidateId, String.valueOf(sortBy), String.valueOf(sortOrder), searchTerm);
    }

    @GetMapping("/expert/{expert-id}/review-requests")
    @PreAuthorize("hasAuthority('read:expert')")
    public List<ReviewRequestSecondViewDto> getListReviewRequest(
            @PathVariable("expert-id") Integer expertId,
            @RequestParam(required = false, defaultValue = "price") SortBy sortBy,
            @RequestParam(required = false, defaultValue = "asc") SortOrder sortOrder,
            @RequestParam(required = false) String searchTerm
    ) {
        return reviewRequestService.getListReviewRequest(expertId, String.valueOf(sortBy), String.valueOf(sortOrder), searchTerm);
    }

}
