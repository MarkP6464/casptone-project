package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Experience;
import com.example.capstoneproject.enums.BasicStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Integer> {
    @Query("SELECT c FROM Experience c WHERE c.cv.id = :cvId AND c.Status = :status")
    List<Experience> findExperiencesByStatus(@Param("cvId") int cvId, @Param("status") BasicStatus status);

    boolean existsByIdAndCv_User_Id(Integer experienceId, Integer UserId);

    @Query("SELECT c FROM Experience c WHERE c.cv.id = :cvId AND c.Status = :status ORDER BY c.id DESC")
    List<Experience> findExperiencesByStatusOrderedByStartDateDesc(@Param("cvId") Integer cvId, @Param("status") BasicStatus status);

}
