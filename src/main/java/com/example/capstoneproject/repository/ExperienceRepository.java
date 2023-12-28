package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Experience;
import com.example.capstoneproject.enums.BasicStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Integer> {
    @Query("SELECT c FROM Experience c WHERE c.cv.id = :UsersId AND c.Status = :status")
    List<Experience> findExperiencesByStatus(@Param("UsersId") int UsersId, @Param("status") BasicStatus status);

    boolean existsByIdAndCv_Id(Integer educationId, Integer cvId);

    @Query("SELECT c FROM Experience c WHERE c.cv.id = :userId AND c.Status = :status ORDER BY c.id DESC")
    List<Experience> findExperiencesByStatusOrderedByStartDateDesc(@Param("userId") Integer userId, @Param("status") BasicStatus status);

}
