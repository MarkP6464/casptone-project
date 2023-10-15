package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.ReviewRequestAddDto;
import com.example.capstoneproject.Dto.ReviewRequestDto;
import com.example.capstoneproject.Dto.responses.ProjectViewDto;
import com.example.capstoneproject.enums.ReviewStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewRequestService extends BaseService<ReviewRequestDto, Integer>{
    ReviewRequestDto createReviewRequest(Integer cvId, Integer expertId, ReviewRequestAddDto dto);
    ReviewRequestDto rejectReviewRequest(Integer expertId, Integer requestId);
    ReviewRequestDto acceptReviewRequest(Integer expertId, Integer requestId);
    List<ReviewRequestDto> getAllReviewRequest(Integer expertId, ReviewStatus reviewStatus, String orderByDate);

}
