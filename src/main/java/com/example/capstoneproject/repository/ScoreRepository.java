package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Integer> {
    Optional<Score> findByCv_Id(Integer cvId);

    @Transactional
    void deleteByCv_Id(Integer cvId);
}
