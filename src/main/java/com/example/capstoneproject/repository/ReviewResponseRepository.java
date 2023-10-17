package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.ReviewResponse;
import com.example.capstoneproject.enums.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewResponseRepository extends JpaRepository<ReviewResponse, Integer> {
    Optional<ReviewResponse> findByReviewRequest_ExpertIdAndIdAndStatus(Integer expertId, Integer responseId, ReviewStatus status);
}
