package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.JobPostingDto;
import com.example.capstoneproject.Dto.responses.JobPostingViewDto;
import com.example.capstoneproject.enums.BasicStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface JobPostingService {
    boolean create(Integer hrId, JobPostingDto dto);
    boolean update(Integer hrId, Integer jobPostingId, JobPostingDto dto);
    boolean delete(Integer hrId, Integer jobPostingId);
    boolean share(Integer hrId, Integer jobPostingId);
    JobPostingViewDto getByHr(Integer hrId, Integer jobPostingId);
    List<JobPostingViewDto> getListByHr(Integer hrId, BasicStatus share);
    List<JobPostingViewDto> getListPublic(Integer cvId, String title, String working, String location) throws JsonProcessingException;
}
