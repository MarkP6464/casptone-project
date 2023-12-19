package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.HR;
import com.example.capstoneproject.entity.HistorySummary;
import com.example.capstoneproject.enums.BasicStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HRRepository extends JpaRepository<HR, Integer> {
    List<HR> findAllByStatusAndVipTrue(BasicStatus status);
}
