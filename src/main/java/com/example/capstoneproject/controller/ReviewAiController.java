package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.responses.ProjectViewDto;
import com.example.capstoneproject.service.ReviewAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1")
public class ReviewAiController {
    @Autowired
    ReviewAiService reviewAiService;

    public ReviewAiController(ReviewAiService reviewAiService) {
        this.reviewAiService = reviewAiService;
    }

    @GetMapping("user/cv/{cv-id}/review-ai")
    @PreAuthorize("hasAnyAuthority('create:candidate','create:expert')")
    public ResponseEntity<?> getListReview(@PathVariable("cv-id") Integer cvId) {
        return ResponseEntity.ok(reviewAiService.getListReviewAi(cvId));
    }
}
