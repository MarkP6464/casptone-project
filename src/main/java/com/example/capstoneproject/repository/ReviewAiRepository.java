package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.ReviewAi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewAiRepository extends JpaRepository<ReviewAi, Integer> {
    List<ReviewAi> findAllByHistory_Id(Integer historyId);
    List<ReviewAi> findAllByHistory_Cv_Id(Integer cvId);
}
