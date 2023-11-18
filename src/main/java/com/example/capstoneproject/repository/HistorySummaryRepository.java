package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.HistorySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistorySummaryRepository extends JpaRepository<HistorySummary, Integer> {
    HistorySummary getById(Integer historySummaryId);
    int countAllByCv_Id(Integer cvId);
    List<HistorySummary> findAllByCv_Id(Integer cvId);
}
