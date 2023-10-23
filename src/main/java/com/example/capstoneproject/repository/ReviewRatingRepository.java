package com.example.capstoneproject.repository;

import com.example.capstoneproject.Dto.ReviewRatingViewDto;
import com.example.capstoneproject.entity.ReviewRating;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRatingRepository extends JpaRepository<ReviewRating, Integer> {
//    List<ReviewRating> findAllByExpertId(Integer expertId);
    List<ReviewRating> findByReviewResponse_ReviewRequest_ExpertIdAndStatus(Integer expertId, BasicStatus status);

    Optional<ReviewRating> findByIdAndUserAndStatus(Integer ratingId, Users user, BasicStatus status);

    Optional<ReviewRating> findByUser_IdAndReviewResponse_Id(Integer userId, Integer responseId);
}
