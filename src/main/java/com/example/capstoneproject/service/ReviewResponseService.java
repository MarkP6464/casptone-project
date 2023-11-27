package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.ReviewRequestViewDto;
import com.example.capstoneproject.Dto.responses.ReviewResponseViewDto;
import com.example.capstoneproject.enums.ReviewStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ReviewResponseService {
    void createReviewResponse(Integer historyId, Integer requestId) throws JsonProcessingException;

    boolean createComment(Integer expertId, Integer responseId, CommentDto dto) throws JsonProcessingException;

    boolean deleteComment(Integer expertId, Integer responseId, String commentId) throws JsonProcessingException;

    boolean updateComment(Integer expertId, Integer responseId, String commentId, CommentNewDto dto) throws JsonProcessingException;

    boolean updateReviewResponse(Integer expertId, Integer responseId, ReviewResponseUpdateDto dto) throws JsonProcessingException;

    boolean publicReviewResponse(Integer expertId, Integer responseId);

    ReviewResponseViewDto receiveReviewResponse(Integer userId, Integer requestId) throws JsonProcessingException;
    ReviewResponseViewDto getReviewResponse(Integer expertId, Integer response) throws JsonProcessingException;
    String sendReviewRating(Integer responseId, ReviewRatingAddDto dto);

}
