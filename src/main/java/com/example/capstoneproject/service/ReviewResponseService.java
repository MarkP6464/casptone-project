package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.CommentDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public interface ReviewResponseService {
    void createReviewResponse(Integer cvId, Integer requestId) throws JsonProcessingException;

    boolean createComment(Integer expertId, Integer responseId, CommentDto dto) throws JsonProcessingException;

    boolean deleteComment(Integer expertId, Integer responseId, String commentId) throws JsonProcessingException;

    boolean updateComment(Integer expertId, Integer responseId, String commentId, String newContent) throws JsonProcessingException;

}
