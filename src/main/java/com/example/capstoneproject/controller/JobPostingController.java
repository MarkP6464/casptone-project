package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.JobPostingDto;
import com.example.capstoneproject.Dto.responses.JobPostingViewDto;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.PublicControl;
import com.example.capstoneproject.service.JobPostingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.capstoneproject.enums.BasicStatus.PRIVATE;
import static com.example.capstoneproject.enums.BasicStatus.PUBLIC;

@RestController
@RequestMapping("/api/v1")
public class JobPostingController {

    @Autowired
    JobPostingService jobPostingService;

    public JobPostingController(JobPostingService jobPostingService) {
        this.jobPostingService = jobPostingService;
    }

    @GetMapping("/hr/{hr-id}/job-postings")
    public List<JobPostingViewDto> getListJobPostingsByHr(
            @PathVariable("hr-id") int hrId,
            @RequestParam(name = "share", required = false) PublicControl share
    ) {
        BasicStatus basicStatus = mapShareToBasicStatus(share);
        return jobPostingService.getListByHr(hrId, basicStatus);
    }

    @GetMapping("/hr/{hr-id}/job-posting/{posting-id}")
    public JobPostingViewDto getAllProject(@PathVariable("hr-id") Integer hrId, @PathVariable("posting-id") Integer postingId) {
        return jobPostingService.getByHr(hrId,postingId);
    }

    @PutMapping("/hr/{hr-id}/job-posting/{posting-id}/share")
    public ResponseEntity<?> sharePosting(@PathVariable("hr-id") Integer hrId, @PathVariable("posting-id") Integer postingId) {
        if (jobPostingService.share(hrId, postingId)) {
            return ResponseEntity.ok("Share success");
        } else {
            return ResponseEntity.badRequest().body("Share failed");
        }
    }

    @DeleteMapping("/hr/{hr-id}/job-posting/{posting-id}")
    public ResponseEntity<?> deletePosting(@PathVariable("hr-id") Integer hrId, @PathVariable("posting-id") Integer postingId) {
        if (jobPostingService.delete(hrId, postingId)) {
            return ResponseEntity.ok("Delete success");
        } else {
            return ResponseEntity.badRequest().body("Delete failed");
        }
    }

    @PutMapping("/hr/{hr-id}/job-posting/{posting-id}")
    public ResponseEntity<?> updatePosting(@PathVariable("hr-id") Integer hrId, @PathVariable("posting-id") Integer postingId, JobPostingDto dto) {
        if (jobPostingService.update(hrId, postingId, dto)) {
            return ResponseEntity.ok("Update success");
        } else {
            return ResponseEntity.badRequest().body("Update failed");
        }
    }

    @PostMapping("/hr/{hr-id}/job-posting")
    public ResponseEntity<?> createPosting(@PathVariable("hr-id") Integer hrId, JobPostingDto dto) {
        if (jobPostingService.create(hrId, dto)) {
            return ResponseEntity.ok("Create success");
        } else {
            return ResponseEntity.badRequest().body("Create failed");
        }
    }

    @GetMapping("/user/cv/job-postings")
    public ResponseEntity<?> searchPostingByCustomer(
            @RequestParam(name = "userId", required = false) Integer userId,
            @RequestParam(name = "cvId", required = false) Integer cvId,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "working", required = false) String working,
            @RequestParam(name = "location", required = false) String location
    ) throws JsonProcessingException {
        return ResponseEntity.ok(jobPostingService.getListPublic(userId, cvId, title, working, location));
    }

    private BasicStatus mapShareToBasicStatus(PublicControl share) {
        if (share == null) {
            return null;
        }
        switch (share) {
            case PUBLIC:
                return PUBLIC;
            case PRIVATE:
                return PRIVATE;
            default:
                throw new IllegalArgumentException("Invalid share value.");
        }
    }
}
