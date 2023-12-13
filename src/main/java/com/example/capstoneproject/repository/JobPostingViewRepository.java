package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.JobPostingView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobPostingViewRepository extends JpaRepository<JobPostingView, Integer> {
    Optional<JobPostingView> findByUser_IdAndJobPosting_Id(Integer userId, Integer postingId);
    List<JobPostingView> findAllByJobPosting_Id(Integer postingId);
}
