package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.ReviewRatingDto;
import org.springframework.stereotype.Service;

@Service
public interface ReviewRatingService {
    boolean updateReviewRating(Integer userId, Integer ratingId, ReviewRatingDto dto);
    ReviewRatingDto createReviewRating(Integer userId, Integer responseId, ReviewRatingDto dto);
    boolean deleteReviewRating(Integer userId, Integer ratingId);
}
