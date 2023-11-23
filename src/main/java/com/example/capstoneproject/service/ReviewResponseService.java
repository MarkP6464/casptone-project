package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.CommentDto;
import com.example.capstoneproject.Dto.ReviewRatingAddDto;
import com.example.capstoneproject.Dto.ReviewResponseDto;
import com.example.capstoneproject.Dto.ReviewResponseUpdateDto;
import com.example.capstoneproject.enums.ReviewStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewResponseService {
    void createReviewResponse(Integer historyId, Integer requestId) throws JsonProcessingException;

    boolean createComment(Integer expertId, Integer responseId, CommentDto dto) throws JsonProcessingException;

    boolean deleteComment(Integer expertId, Integer responseId, String commentId) throws JsonProcessingException;

    boolean updateComment(Integer expertId, Integer responseId, String commentId, String newContent) throws JsonProcessingException;

    boolean updateReviewResponse(Integer expertId, Integer responseId, ReviewResponseUpdateDto dto);

    boolean publicReviewResponse(Integer expertId, Integer responseId);

    ReviewResponseDto receiveReviewResponse(Integer userId, Integer requestId) throws JsonProcessingException;
    ReviewResponseDto getReviewResponse(Integer expertId, Integer response) throws JsonProcessingException;
    String sendReviewRating(Integer responseId, ReviewRatingAddDto dto);

}
