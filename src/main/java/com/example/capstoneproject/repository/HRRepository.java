package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.HR;
import com.example.capstoneproject.entity.HistorySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HRRepository extends JpaRepository<HR, Integer> {
}
