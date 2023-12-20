package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.ReviewRequest;
import com.example.capstoneproject.entity.ReviewResponse;
import com.example.capstoneproject.enums.ReviewStatus;
import com.example.capstoneproject.enums.StatusReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    List<ReviewRequest> findAllByExpertIdAndStatus(Integer expertId, StatusReview status);

    Optional<ReviewRequest> findByExpertIdAndId(Integer expertId, Integer requestId);
    List<ReviewRequest> findAllByExpertId(Integer expertId);
    List<ReviewRequest> findAllByCv_User_Id(Integer userId);

    List<ReviewRequest> findAllByDeadline(LocalDateTime deadline);

    Optional<ReviewRequest> findByIdAndStatus(Integer requestId, StatusReview reviewStatus);

    int countByExpertIdAndStatus(Integer expertId, StatusReview status);

    Optional<ReviewRequest> findByIdAndStatusNot(Integer id, StatusReview status);
}
