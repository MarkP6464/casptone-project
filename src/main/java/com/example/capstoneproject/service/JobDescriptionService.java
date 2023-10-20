package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.JobDescriptionDto;
import com.example.capstoneproject.Dto.JobDescriptionViewDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public interface JobDescriptionService extends BaseService<JobDescriptionViewDto, Integer>{
    JobDescriptionViewDto createJobDescription(Integer cvId, JobDescriptionDto dto) throws JsonProcessingException;

    JobDescriptionViewDto getJobDescription(Integer cvId, Integer jobId) throws JsonProcessingException;

    JobDescriptionViewDto updateJobDescription(Integer cvId, Integer jobId, JobDescriptionDto dto) throws Exception;
}
