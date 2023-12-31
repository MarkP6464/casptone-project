package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.JobDescriptionDto;
import com.example.capstoneproject.Dto.JobDescriptionViewDto;
import com.example.capstoneproject.Dto.request.JobDescriptionRequest;
import com.example.capstoneproject.service.JobDescriptionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
public class JobDescriptionController {

    @Autowired
    JobDescriptionService jobDescriptionService;

    @PostMapping("cv/{cv-id}/job-description")
    @PreAuthorize("hasAnyAuthority('create:candidate','create:expert')")
    public JobDescriptionViewDto postJobDescription(@PathVariable("cv-id") int cvId, @RequestBody JobDescriptionRequest Dto, Principal principal) throws JsonProcessingException {
        return jobDescriptionService.createJobDescription(cvId,Dto,principal);
    }

    @GetMapping("cv/{cv-id}/job-description/ats")
    @PreAuthorize("hasAnyAuthority('read:candidate','read:expert')")
    public JobDescriptionViewDto getJobDescription(@PathVariable("cv-id") Integer cvId) throws JsonProcessingException {
        return jobDescriptionService.getJobDescription(cvId);
    }

    @PutMapping("cv/{cv-id}/job-description/ats")
    @PreAuthorize("hasAnyAuthority('update:candidate','update:expert')")
    public JobDescriptionViewDto putJobDescription(@PathVariable("cv-id") Integer cvId, @RequestBody JobDescriptionRequest Dto, Principal principal) throws Exception {
        return jobDescriptionService.updateJobDescription(cvId,Dto,principal);
    }
}
