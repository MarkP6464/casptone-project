package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.JobPostingAddDto;
import com.example.capstoneproject.Dto.JobPostingDto;
import com.example.capstoneproject.Dto.JobPostingViewOverCandidateLikeDto;
import com.example.capstoneproject.Dto.responses.JobPostingViewDetailDto;
import com.example.capstoneproject.Dto.responses.JobPostingViewDto;
import com.example.capstoneproject.Dto.responses.JobPostingViewUserDetailDto;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.PublicControl;
import com.example.capstoneproject.enums.SortByJob;
import com.example.capstoneproject.enums.SortOrder;
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
    public ResponseEntity<?> updatePosting(@PathVariable("hr-id") Integer hrId, @PathVariable("posting-id") Integer postingId, JobPostingAddDto dto) {
        if (jobPostingService.update(hrId, postingId, dto)) {
            return ResponseEntity.ok("Update success");
        } else {
            return ResponseEntity.badRequest().body("Update failed");
        }
    }

    @PostMapping("/hr/{hr-id}/job-posting/draft")
    @PreAuthorize("hasAuthority('create:hr')")
    public ResponseEntity<?> createDaftPosting(@PathVariable("hr-id") Integer hrId, JobPostingAddDto dto) {
        if (jobPostingService.createDraft(hrId, dto)) {
            return ResponseEntity.ok("Successfully sent job posting.");
        } else {
            return ResponseEntity.badRequest().body("Fail sent job posting.");
        }
    }
    @PostMapping("/hr/{hr-id}/job-posting/public")
    @PreAuthorize("hasAuthority('create:hr')")
    public ResponseEntity<?> createPublicPosting(@PathVariable("hr-id") Integer hrId, JobPostingAddDto dto) {
        if (jobPostingService.createPublic(hrId, dto)) {
            return ResponseEntity.ok("Successfully sent job posting.");
        } else {
            return ResponseEntity.badRequest().body("Fail sent job posting.");
        }
    }

//    @GetMapping("/user/cv/job-postings")
//    @PreAuthorize("hasRole('ROLE_CANDIDATE')")
//    public ResponseEntity<?> searchPostingByCustomer(
//            @RequestParam(name = "userId", required = false) Integer userId,
//            @RequestParam(name = "cvId", required = false) Integer cvId,
//            @RequestParam(name = "title", required = false) String title,
//            @RequestParam(name = "working", required = false) String working,
//            @RequestParam(name = "location", required = false) String location
//    ) throws JsonProcessingException {
//        return ResponseEntity.ok(jobPostingService.getListPublic(userId, cvId, title, working, location));
//    }
//
//    private BasicStatus mapShareToBasicStatus(PublicControl share) {
//        if (share == null) {
//            return null;
//        }
//        switch (share) {
//            case PUBLIC:
//                return PUBLIC;
//            case PRIVATE:
//                return PRIVATE;
//            default:
//                throw new IllegalArgumentException("Invalid share value.");
//        }
//    }
}
