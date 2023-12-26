package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.JobPostingAddDto;
import com.example.capstoneproject.Dto.JobPostingViewOverCandidateLikeDto;
import com.example.capstoneproject.Dto.responses.*;
import com.example.capstoneproject.enums.SortByJob;
import com.example.capstoneproject.enums.SortOrder;
import com.example.capstoneproject.service.HRService;
import com.example.capstoneproject.service.JobPostingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class JobPostingController {
    @Autowired
    HRService hrService;

    @Autowired
    JobPostingService jobPostingService;

    public JobPostingController(JobPostingService jobPostingService) {
        this.jobPostingService = jobPostingService;
    }

    @GetMapping("/hr/{hr-id}/job-postings")
    @PreAuthorize("hasAuthority('read:hr')")
    public List<JobPostingViewDetailDto> getListJobPostingsByHr(
            @PathVariable("hr-id") int hrId,
            @RequestParam(name = "sortBy", required = false, defaultValue = "view") SortByJob sortBy,
            @RequestParam(name = "sortOrder", required = false, defaultValue = "asc") SortOrder sortOrder,
            @RequestParam(name = "searchTerm", required = false) String searchTerm
    ) {
        return jobPostingService.getListByHr(hrId, String.valueOf(sortBy), String.valueOf(sortOrder), searchTerm);
    }

    @GetMapping("/user/{user-id}/job-posting")
    @PreAuthorize("hasAnyAuthority('read:candidate','read:expert','read:hr')")
    public ResponseEntity<List<JobPostingViewOverCandidateLikeDto>> getJobPostingsByCandidate(
            @PathVariable("user-id") Integer userId,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "location", required = false) String location) {

        List<JobPostingViewOverCandidateLikeDto> jobPostings = jobPostingService.getJobPostingsByCandidate(userId, title, location);

        return ResponseEntity.ok(jobPostings);
    }

    @GetMapping("/hr/job-posting/{job-posting-id}/match/skills")
    @PreAuthorize("hasAnyAuthority('read:hr')")
    public ResponseEntity<?> getJobPostingsByCandidateMatching(
            @PathVariable("job-posting-id") Integer postingId) throws JsonProcessingException {
        List<CandidateOverViewDto> jobPostings = jobPostingService.getAllCandidateFilterCV(postingId);

        return ResponseEntity.ok(jobPostings);
    }

    @GetMapping("/hr/{hr-id}/job-posting/{posting-id}")
    @PreAuthorize("hasAuthority('read:hr')")
    public JobPostingViewDto getJobDetailHr(@PathVariable("hr-id") Integer hrId, @PathVariable("posting-id") Integer postingId) {
        return jobPostingService.getByHr(hrId,postingId);
    }

    @GetMapping("/user/{user-id}/job-posting/{posting-id}")
    @PreAuthorize("hasAnyAuthority('read:candidate','read:expert','read:hr')")
    public JobPostingViewUserDetailDto getJobDetailUser(@PathVariable("user-id") Integer userId, @PathVariable("posting-id") Integer postingId) {
        return jobPostingService.getByUser(userId,postingId);
    }

    @PutMapping("/hr/{hr-id}/job-posting/{posting-id}/share")
    @PreAuthorize("hasAuthority('update:hr')")
    public ResponseEntity<?> sharePosting(@PathVariable("hr-id") Integer hrId, @PathVariable("posting-id") Integer postingId) {
        if (jobPostingService.share(hrId, postingId)) {
            return ResponseEntity.ok("Share success");
        } else {
            return ResponseEntity.badRequest().body("Share failed");
        }
    }

    @PutMapping("/hr/{hr-id}/job-posting/{posting-id}/un-share")
    @PreAuthorize("hasAuthority('update:hr')")
    public ResponseEntity<?> unSharePosting(@PathVariable("hr-id") Integer hrId, @PathVariable("posting-id") Integer postingId) {
        if (jobPostingService.unShare(hrId, postingId)) {
            return ResponseEntity.ok("Un Share success");
        } else {
            return ResponseEntity.badRequest().body("Un Share failed");
        }
    }

    @DeleteMapping("/hr/{hr-id}/job-posting/{posting-id}")
    @PreAuthorize("hasAuthority('delete:hr')")
    public ResponseEntity<?> deletePosting(@PathVariable("hr-id") Integer hrId, @PathVariable("posting-id") Integer postingId) {
        if (jobPostingService.delete(hrId, postingId)) {
            return ResponseEntity.ok("Delete success");
        } else {
            return ResponseEntity.badRequest().body("Delete failed");
        }
    }

    @PutMapping("/hr/{hr-id}/job-posting/{posting-id}")
    @PreAuthorize("hasAuthority('update:hr')")
    public ResponseEntity<?> updatePosting(@PathVariable("hr-id") Integer hrId, @PathVariable("posting-id") Integer postingId, @RequestBody JobPostingAddDto dto) {
        if (jobPostingService.update(hrId, postingId, dto)) {
            return ResponseEntity.ok("Update success");
        } else {
            return ResponseEntity.badRequest().body("Update failed");
        }
    }

    @PostMapping("/hr/{hr-id}/job-posting/draft")
    @PreAuthorize("hasAuthority('create:hr')")
    public ResponseEntity<?> createDaftPosting(@PathVariable("hr-id") Integer hrId, @RequestBody JobPostingAddDto dto) {
        if (jobPostingService.createDraft(hrId, dto)) {
            return ResponseEntity.ok("Successfully sent job posting.");
        } else {
            return ResponseEntity.badRequest().body("Fail sent job posting.");
        }
    }
    @PostMapping("/hr/{hr-id}/job-posting/public")
    @PreAuthorize("hasAuthority('create:hr')")
    public ResponseEntity<?> createPublicPosting(@PathVariable("hr-id") Integer hrId, @RequestBody JobPostingAddDto dto) {
        if (jobPostingService.createPublic(hrId, dto)) {
            return ResponseEntity.ok("Successfully sent job posting.");
        } else {
            return ResponseEntity.badRequest().body("Fail sent job posting.");
        }
    }

    @PutMapping("/admin/{admin-id}/job-posting/{posting-id}/ban")
    @PreAuthorize("hasAuthority('update:admin')")
    public ResponseEntity<?> updateBan(@PathVariable("admin-id") Integer adminId, @PathVariable("posting-id") Integer postingId) {
        return ResponseEntity.ok(jobPostingService.updateBan(adminId, postingId));
    }

    @PutMapping("/admin/{admin-id}/job-posting/{posting-id}/un-ban")
    @PreAuthorize("hasAuthority('update:admin')")
    public ResponseEntity<?> updateUnBan(@PathVariable("admin-id") Integer adminId, @PathVariable("posting-id") Integer postingId) {
        return ResponseEntity.ok(jobPostingService.updateUnBan(adminId, postingId));
    }

    @GetMapping("/admin/{admin-id}/job-postings")
    @PreAuthorize("hasAuthority('read:admin')")
    public ResponseEntity<?> getListAdminPosting(@PathVariable("admin-id") Integer adminId) {
        return ResponseEntity.ok(jobPostingService.getListAdminPosting(adminId));
    }

    @GetMapping("job-postings/generation/description")
    public ResponseEntity<?> getJobPostings(@RequestParam(required = false) String search) {
        List<JobPostingResponse> jobPostings = jobPostingService.getListGeneration(search);
        return ResponseEntity.ok(jobPostings);
    }

}
