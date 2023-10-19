package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.JobPostingDto;
import com.example.capstoneproject.Dto.ReviewRatingDto;
import com.example.capstoneproject.Dto.responses.JobPostingViewDto;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.service.JobPostingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class JobPostingController {

    @Autowired
    JobPostingService jobPostingService;

    public JobPostingController(JobPostingService jobPostingService) {
        this.jobPostingService = jobPostingService;
    }

    @GetMapping("/hr/{hrId}/job-postings")
    public List<JobPostingViewDto> getListJobPostingsByHr(
            @PathVariable("hrId") int hrId,
            @RequestParam(name = "share", required = false) String share
    ) {
        BasicStatus basicStatus = mapShareToBasicStatus(share);
        return jobPostingService.getListByHr(hrId, basicStatus);
    }

    @GetMapping("/hr/{hrId}/job-posting/{postingId}")
    public JobPostingViewDto getAllProject(@PathVariable("hrId") Integer hrId, @PathVariable("postingId") Integer postingId) {
        return jobPostingService.getByHr(hrId,postingId);
    }

    @PutMapping("/hr/{hrId}/job-posting/{postingId}/share")
    public ResponseEntity<?> sharePosting(@PathVariable("hrId") Integer hrId, @PathVariable("postingId") Integer postingId) {
        if (jobPostingService.share(hrId, postingId)) {
            return ResponseEntity.ok("Share success");
        } else {
            return ResponseEntity.badRequest().body("Share failed");
        }
    }

    @DeleteMapping("/hr/{hrId}/job-posting/{postingId}")
    public ResponseEntity<?> deletePosting(@PathVariable("hrId") Integer hrId, @PathVariable("postingId") Integer postingId) {
        if (jobPostingService.delete(hrId, postingId)) {
            return ResponseEntity.ok("Delete success");
        } else {
            return ResponseEntity.badRequest().body("Delete failed");
        }
    }

    @PutMapping("/hr/{hrId}/job-posting/{postingId}")
    public ResponseEntity<?> updatePosting(@PathVariable("hrId") Integer hrId, @PathVariable("postingId") Integer postingId, JobPostingDto dto) {
        if (jobPostingService.update(hrId, postingId, dto)) {
            return ResponseEntity.ok("Update success");
        } else {
            return ResponseEntity.badRequest().body("Update failed");
        }
    }

    @PostMapping("/hr/{hrId}/job-posting")
    public ResponseEntity<?> createPosting(@PathVariable("hrId") Integer hrId, JobPostingDto dto) {
        if (jobPostingService.create(hrId, dto)) {
            return ResponseEntity.ok("Create success");
        } else {
            return ResponseEntity.badRequest().body("Create failed");
        }
    }

    @GetMapping("/user/cv/job-postings")
    public ResponseEntity<?> searchPostingByCustomer(
            @RequestParam(name = "cvId", required = false) Integer cvId,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "working", required = false) String working,
            @RequestParam(name = "location", required = false) String location
    ) throws JsonProcessingException {
        return ResponseEntity.ok(jobPostingService.getListPublic(cvId, title, working, location));
    }

    private BasicStatus mapShareToBasicStatus(String share) {
        if (share == null) {
            return null;
        }
        switch (share) {
            case "public":
                return BasicStatus.PUBLIC;
            case "private":
                return BasicStatus.PRIVATE;
            default:
                throw new IllegalArgumentException("Invalid share value.");
        }
    }
}
