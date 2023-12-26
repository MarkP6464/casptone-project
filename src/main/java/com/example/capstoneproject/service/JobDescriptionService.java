package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.JobDescriptionDto;
import com.example.capstoneproject.Dto.JobDescriptionViewDto;
import com.example.capstoneproject.Dto.request.JobDescriptionRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public interface JobDescriptionService extends BaseService<JobDescriptionViewDto, Integer>{
    JobDescriptionViewDto createJobDescription(Integer cvId, JobDescriptionRequest dto, Principal principal) throws JsonProcessingException;

    JobDescriptionViewDto getJobDescription(Integer cvId) throws JsonProcessingException;

    JobDescriptionViewDto updateJobDescription(Integer cvId, JobDescriptionRequest dto, Principal principal) throws Exception;
}
