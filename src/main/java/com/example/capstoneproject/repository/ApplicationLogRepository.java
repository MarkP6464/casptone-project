package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.ApplicationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationLogRepository extends JpaRepository<ApplicationLog, Integer> {
    List<ApplicationLog> findAllByUser_IdAndJobPosting_IdOrderByTimestampDesc(Integer userId, Integer postingId);
}
