package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Certification;
import com.example.capstoneproject.entity.Experience;
import com.example.capstoneproject.enums.CvStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Integer> {
    @Query("SELECT c FROM Experience c WHERE c.cv.id = :cvId AND c.Status = :status")
    List<Experience> findExperiencesByStatus(@Param("cvId") int id, @Param("status") CvStatus status);

    boolean existsByIdAndCv_Id(Integer experienceId, Integer cvId);
}
