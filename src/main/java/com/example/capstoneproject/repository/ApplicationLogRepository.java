package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.ApplicationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationLogRepository extends JpaRepository<ApplicationLog, Integer> {
}
