package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.ScoreLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ScoreLogRepository extends JpaRepository<ScoreLog, Integer> {

    @Transactional
    void deleteAllByScore_Id(Integer scoreId);
}
