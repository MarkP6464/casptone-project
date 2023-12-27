package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Experience;
import com.example.capstoneproject.entity.Project;
import com.example.capstoneproject.enums.BasicStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query("SELECT c FROM Project c WHERE c.cv.user.id = :cvId AND c.Status = :status")
    List<Project> findProjectsByStatus(@Param("cvId") int id, @Param("status") BasicStatus status);
    boolean existsByIdAndCv_User_Id(Integer projectId, Integer UserId);

    @Query("SELECT c FROM Project c WHERE c.cv.user.id = :userId AND c.Status = :status ORDER BY c.id DESC")
    List<Project> findExperiencesByStatusOrderedByStartDateDesc(@Param("userId") Integer userId, @Param("status") BasicStatus status);


}
