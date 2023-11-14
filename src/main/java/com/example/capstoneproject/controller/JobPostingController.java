package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.JobPostingDto;
import com.example.capstoneproject.Dto.responses.JobPostingViewDetailDto;
import com.example.capstoneproject.Dto.responses.JobPostingViewDto;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.PublicControl;
import com.example.capstoneproject.service.JobPostingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public List<JobPostingViewDetailDto> getListJobPostingsByHr(
            @PathVariable("hr-id") int hrId,
            @RequestParam(name = "sortBy", required = false, defaultValue = "createdate") String sortBy,
            @RequestParam(name = "sortOrder", required = false, defaultValue = "asc") String sortOrder,
            @RequestParam(name = "searchTerm", required = false) String searchTerm
    ) {

        return jobPostingService.getListByHr(hrId, sortBy, sortOrder, searchTerm);
    }

    @GetMapping("/hr/{hr-id}/job-posting/{posting-id}")
    @PreAuthorize("hasRole('ROLE_HR')")
    public JobPostingViewDto getAllProject(@PathVariable("hr-id") Integer hrId, @PathVariable("posting-id") Integer postingId) {
        return jobPostingService.getByHr(hrId,postingId);
    }

    @PutMapping("/hr/{hr-id}/job-posting/{posting-id}/share")
    @PreAuthorize("hasRole('ROLE_HR')")
    public ResponseEntity<?> sharePosting(@PathVariable("hr-id") Integer hrId, @PathVariable("posting-id") Integer postingId) {
        if (jobPostingService.share(hrId, postingId)) {
            return ResponseEntity.ok("Share success");
        } else {
            return ResponseEntity.badRequest().body("Share failed");
        }
    }

    @DeleteMapping("/hr/{hr-id}/job-posting/{posting-id}")
    @PreAuthorize("hasRole('ROLE_HR')")
    public ResponseEntity<?> deletePosting(@PathVariable("hr-id") Integer hrId, @PathVariable("posting-id") Integer postingId) {
        if (jobPostingService.delete(hrId, postingId)) {
            return ResponseEntity.ok("Delete success");
        } else {
            return ResponseEntity.badRequest().body("Delete failed");
        }
    }

    @PutMapping("/hr/{hr-id}/job-posting/{posting-id}")
    @PreAuthorize("hasRole('ROLE_HR')")
    public ResponseEntity<?> updatePosting(@PathVariable("hr-id") Integer hrId, @PathVariable("posting-id") Integer postingId, JobPostingDto dto) {
        if (jobPostingService.update(hrId, postingId, dto)) {
            return ResponseEntity.ok("Update success");
        } else {
            return ResponseEntity.badRequest().body("Update failed");
        }
    }

    @PostMapping("/hr/{hr-id}/job-posting")
    @PreAuthorize("hasRole('ROLE_HR')")
    public ResponseEntity<?> createPosting(@PathVariable("hr-id") Integer hrId, JobPostingDto dto) {
        if (jobPostingService.create(hrId, dto)) {
            return ResponseEntity.ok("Successfully sent job posting.");
        } else {
            return ResponseEntity.badRequest().body("Fail sent job posting.");
        }
    }

    @GetMapping("/user/cv/job-postings")
    @PreAuthorize("hasRole('ROLE_CANDIDATE')")
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
