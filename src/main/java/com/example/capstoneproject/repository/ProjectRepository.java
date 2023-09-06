package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Involvement;
import com.example.capstoneproject.entity.Project;
import com.example.capstoneproject.enums.CvStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query("SELECT c FROM Project c WHERE c.cv.id = :cvId AND c.Status = :status")
    List<Project> findProjectsByStatus(@Param("cvId") int id, @Param("status") CvStatus status);
}
