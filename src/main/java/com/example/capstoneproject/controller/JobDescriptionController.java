package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.JobDescriptionDto;
import com.example.capstoneproject.Dto.JobDescriptionViewDto;
import com.example.capstoneproject.service.JobDescriptionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class JobDescriptionController {

    @Autowired
    JobDescriptionService jobDescriptionService;

    @PostMapping("cv/{cv-id}/job-description")
    public JobDescriptionViewDto postJobDescription(@PathVariable("cv-id") int cvId, @RequestBody JobDescriptionDto Dto) throws JsonProcessingException {
        return jobDescriptionService.createJobDescription(cvId,Dto);
    }

    @GetMapping("cv/{cv-id}/job-description/{job-description-id}")
    public JobDescriptionViewDto getJobDescription(@PathVariable("cv-id") int cvId, @PathVariable("job-description-id") int jobId) throws JsonProcessingException {
        return jobDescriptionService.getJobDescription(cvId,jobId);
    }

    @PutMapping("cv/{cv-id}/job-description/{job-description-id}")
    public JobDescriptionViewDto putJobDescription(@PathVariable("cv-id") int cvId, @PathVariable("job-description-id") int jobId, @RequestBody JobDescriptionDto Dto) throws Exception {
        return jobDescriptionService.updateJobDescription(cvId,jobId,Dto);
    }
}
