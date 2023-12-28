package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Education;
import com.example.capstoneproject.enums.BasicStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EducationRepository extends JpaRepository<Education, Integer> {
    @Query("SELECT c FROM Education c WHERE c.cv.id = :UsersId AND c.Status = :status")
    List<Education> findEducationsByStatus(@Param("UsersId") int UsersId,@Param("status") BasicStatus status);

    boolean existsByIdAndCv_Id(Integer educationId, Integer cvId);

}
