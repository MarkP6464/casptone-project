package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.responses.JobPostingViewOverCandidateDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LikeService {
    String likeJobPosting(Integer userId, Integer jobPostingId);
    List<JobPostingViewOverCandidateDto> getJobPostingLiked(Integer userId);
}
