package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.ReviewRequestAddDto;
import com.example.capstoneproject.Dto.ReviewRequestDto;
import com.example.capstoneproject.Dto.responses.*;
import com.example.capstoneproject.enums.ReviewStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewRequestService extends BaseService<ReviewRequestDto, Integer>{
    String createReviewRequest(Integer cvId, Integer expertId, Integer optionId, ReviewRequestAddDto dto) throws JsonProcessingException;
    List<ReviewRequestSecondViewDto> getListReviewRequest(Integer expertId, String sortBy, String sortOrder, String searchTerm);
    List<ReviewRequestHistorySecondViewDto> getListReviewRequestHistory(Integer expertId, String sortBy, String sortOrder, String searchTerm);
    List<ReviewRequestCandidateSecondViewDto> getListReviewRequestCandidate(Integer userId, String sortBy, String sortOrder, String searchTerm);
    String rejectReviewRequest(Integer expertId, Integer requestId);
    String acceptReviewRequest(Integer expertId, Integer requestId) throws JsonProcessingException;

}
