package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.ReviewAiDto;
import com.example.capstoneproject.Dto.responses.ReviewAiViewDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewAiService {
    void createReviewAi(Integer cvId, ReviewAiDto dto);

    List<ReviewAiViewDto> getListReviewAi(Integer cvId);
}
