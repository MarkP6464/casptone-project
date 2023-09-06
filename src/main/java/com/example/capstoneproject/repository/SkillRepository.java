package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Project;
import com.example.capstoneproject.entity.Skill;
import com.example.capstoneproject.enums.CvStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Integer> {
    @Query("SELECT c FROM Skill c WHERE c.cv.id = :cvId AND c.Status = :status")
    List<Skill> findSkillsByStatus(@Param("cvId") int id, @Param("status") CvStatus status);
}
