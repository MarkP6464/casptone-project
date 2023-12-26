package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.JobPostingAddDto;
import com.example.capstoneproject.Dto.JobPostingDto;
import com.example.capstoneproject.Dto.JobPostingViewOverCandidateLikeDto;
import com.example.capstoneproject.Dto.responses.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface JobPostingService {
    boolean createDraft(Integer hrId, JobPostingAddDto dto);
    boolean createPublic(Integer hrId, JobPostingAddDto dto);
    boolean update(Integer hrId, Integer jobPostingId, JobPostingAddDto dto);
    boolean delete(Integer hrId, Integer jobPostingId);
    boolean share(Integer hrId, Integer jobPostingId);
    boolean unShare(Integer hrId, Integer jobPostingId);
    JobPostingViewDto getByHr(Integer hrId, Integer jobPostingId);
    JobPostingViewUserDetailDto getByUser(Integer userId, Integer jobPostingId);
    List<JobPostingViewDetailDto> getListByHr(Integer hrId, String sortBy, String sortOrder, String searchTerm);
    List<JobPostingViewOverCandidateLikeDto> getJobPostingsByCandidate(Integer userId, String title, String location);
    List<JobPostingViewDto> getListPublic(Integer userId, Integer cvId, String title, String working, String location) throws JsonProcessingException;
    List<CandidateOverViewDto> getAllCandidateFilterCV(Integer postingId) throws JsonProcessingException;
    String updateBan(Integer adminId, Integer postingId);
    String updateUnBan(Integer adminId, Integer postingId);
    List<JobPostingAdminViewDto> getListAdminPosting(Integer adminId);
    List<JobPostingResponse> getListGeneration(String search);

}
