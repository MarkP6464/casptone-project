package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.JobPostingDto;
import com.example.capstoneproject.Dto.responses.JobPostingViewDetailDto;
import com.example.capstoneproject.Dto.responses.JobPostingViewDto;
import com.example.capstoneproject.Dto.responses.JobPostingViewOverCandidateDto;
import com.example.capstoneproject.Dto.responses.JobPostingViewOverDto;
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
    List<JobPostingViewDetailDto> getListByHr(Integer hrId, String sortBy, String sortOrder, String searchTerm);
    List<JobPostingViewOverCandidateDto> getJobPostingsByCandidate(String title, String location);
    List<JobPostingViewDto> getListPublic(Integer userId, Integer cvId, String title, String working, String location) throws JsonProcessingException;

}
