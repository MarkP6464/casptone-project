package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.ReviewRequestAddDto;
import com.example.capstoneproject.Dto.ReviewRequestDto;
import com.example.capstoneproject.Dto.responses.ProjectViewDto;
import com.example.capstoneproject.Dto.responses.ReviewRequestSecondViewDto;
import com.example.capstoneproject.Dto.responses.ReviewRequestViewDto;
import com.example.capstoneproject.enums.ReviewStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewRequestService extends BaseService<ReviewRequestDto, Integer>{
    ReviewRequestDto createReviewRequest(Integer cvId, Integer expertId, ReviewRequestAddDto dto) throws JsonProcessingException;
//    List<ReviewRequestDto> getAllReviewRequest(Integer expertId, ReviewStatus reviewStatus, String orderByDate);
    List<ReviewRequestSecondViewDto> getListReviewRequest(Integer expertId, String sortBy, String sortOrder, String searchTerm);

    List<ReviewRequestSecondViewDto> getListReviewRequestCandidate(Integer userId, String sortBy, String sortOrder, String searchTerm);

}
