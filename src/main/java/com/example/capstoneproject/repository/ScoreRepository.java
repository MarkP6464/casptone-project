package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Integer> {
    Optional<Score> findByCv_Id(Integer cvId);

    @Query("SELECT s FROM Score s WHERE s.id = :scoreId")
    Score findById1(@Param("scoreId") Integer scoreId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Score s WHERE s.id = :scoreId")
    void deleteScoreById(@Param("scoreId") Integer scoreId);
}
