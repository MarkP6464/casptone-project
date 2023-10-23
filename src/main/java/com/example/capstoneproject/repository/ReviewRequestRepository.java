package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.ReviewRequest;
import com.example.capstoneproject.enums.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRequestRepository extends JpaRepository<ReviewRequest, Integer> {
    List<ReviewRequest> findAllByExpertIdAndStatus(Integer expertId, ReviewStatus status);
    @Query("SELECT rr FROM ReviewRequest rr WHERE rr.expertId = :expertId AND rr.id = :id AND rr.status = :status")
    Optional<ReviewRequest> findReviewRequestByExpertIdAndIdAndStatus(
            @Param("expertId") Integer expertId,
            @Param("id") Integer id,
            @Param("status") ReviewStatus status
    );

    Optional<ReviewRequest> findByIdAndStatus(Integer requestId, ReviewStatus reviewStatus);
}
