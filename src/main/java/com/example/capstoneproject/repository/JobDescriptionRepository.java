package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.JobDescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobDescriptionRepository extends JpaRepository<JobDescription, Integer> {
    Optional<JobDescription> findById(Integer jobId);
}
