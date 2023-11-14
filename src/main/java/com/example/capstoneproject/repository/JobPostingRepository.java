package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.JobPosting;
import com.example.capstoneproject.enums.BasicStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting, Integer> {
    Optional<JobPosting> findByUser_IdAndIdAndStatus(Integer userId, Integer postingId, BasicStatus status);
    List<JobPosting> findByUser_IdAndStatus(Integer userId, BasicStatus status);

    List<JobPosting> findByShare(BasicStatus status);
    Optional<JobPosting> findByIdAndStatusAndShare(Integer postingId, BasicStatus status, BasicStatus share);
    List<JobPosting> findAllByDeadline(LocalDate deadline);
}
