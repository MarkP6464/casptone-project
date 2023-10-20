package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.ReviewResponse;
import com.example.capstoneproject.enums.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewResponseRepository extends JpaRepository<ReviewResponse, Integer> {
    Optional<ReviewResponse> findByReviewRequest_ExpertIdAndIdAndStatus(Integer expertId, Integer responseId, ReviewStatus status);

    Optional<ReviewResponse> findByReviewRequest_IdAndStatus(Integer requestId, ReviewStatus status);

    List<ReviewResponse> findByReviewRequest_ExpertId(Integer expertId);

    Optional<ReviewResponse> findByIdAndStatus(Integer responseId, ReviewStatus status);

    Optional<ReviewResponse> findByReviewRequest_ExpertIdAndId(Integer expertId, Integer responseId);

}
