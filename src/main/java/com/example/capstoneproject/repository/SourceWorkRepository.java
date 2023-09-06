package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Skill;
import com.example.capstoneproject.entity.SourceWork;
import com.example.capstoneproject.enums.CvStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SourceWorkRepository extends JpaRepository<SourceWork, Integer> {
    @Query("SELECT c FROM SourceWork c WHERE c.cv.id = :cvId AND c.Status = :status")
    List<SourceWork> findSourceWorksByStatus(@Param("cvId") int id, @Param("status") CvStatus status);
}
