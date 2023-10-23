package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.JobDescriptionDto;
import com.example.capstoneproject.Dto.JobDescriptionViewDto;
import com.example.capstoneproject.service.JobDescriptionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/Users")
public class JobDescriptionController {

    @Autowired
    JobDescriptionService jobDescriptionService;

    @PostMapping("cv/{cvId}/job-description")
    public JobDescriptionViewDto postJobDescription(@PathVariable("cvId") int cvId, @RequestBody JobDescriptionDto Dto) throws JsonProcessingException {
        return jobDescriptionService.createJobDescription(cvId,Dto);
    }

    @GetMapping("cv/{cvId}/job-description/{jobId}")
    public JobDescriptionViewDto getJobDescription(@PathVariable("cvId") int cvId, @PathVariable("jobId") int jobId) throws JsonProcessingException {
        return jobDescriptionService.getJobDescription(cvId,jobId);
    }

    @PutMapping("cv/{cvId}/job-description/{jobId}")
    public JobDescriptionViewDto putJobDescription(@PathVariable("cvId") int cvId, @PathVariable("jobId") int jobId, @RequestBody JobDescriptionDto Dto) throws Exception {
        return jobDescriptionService.updateJobDescription(cvId,jobId,Dto);
    }
}
