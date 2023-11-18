package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.responses.JobPostingViewOverCandidateDto;
import com.example.capstoneproject.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LikeController {

    @Autowired
    LikeService likeService;

    @PostMapping("/user/{user-id}/job-posting/{job-posting-id}/like")
    @PreAuthorize("hasAnyAuthority('create:candidate','create:expert','create:hr')")
    public ResponseEntity<String> likeJobPosting(
            @PathVariable("user-id") Integer userId,
            @PathVariable("job-posting-id") Integer jobId) {
        String result = likeService.likeJobPosting(userId, jobId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{user-id}/job-postings/like")
    @PreAuthorize("hasAnyAuthority('read:candidate','read:expert','read:hr')")
    public ResponseEntity<List<JobPostingViewOverCandidateDto>> getJobPostingLiked(
            @PathVariable("user-id") Integer userId) {
        List<JobPostingViewOverCandidateDto> likedJobPostings = likeService.getJobPostingLiked(userId);
        return ResponseEntity.ok(likedJobPostings);
    }
}
