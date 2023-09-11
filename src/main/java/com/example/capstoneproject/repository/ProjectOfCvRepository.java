package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Involvement;
import com.example.capstoneproject.entity.InvolvementOfCv;
import com.example.capstoneproject.entity.Project;
import com.example.capstoneproject.entity.ProjectOfCv;
import com.example.capstoneproject.enums.CvStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectOfCvRepository extends JpaRepository<ProjectOfCv, Integer> {
    ProjectOfCv findByProject_IdAndCv_Id(Integer involvementId, Integer cvId);
    @Query("SELECT s FROM ProjectOfCv s WHERE s.cv.id = :cvId AND s.project.Status = :status")
    List<ProjectOfCv> findActiveProjectsByCvId(@Param("cvId") int cvId, @Param("status") CvStatus status);
    @Query("SELECT s.project FROM ProjectOfCv s WHERE s.cv.id = :cvId")
    List<Project> findProjectsByCvId(Integer cvId);
}
