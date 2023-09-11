package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Education;
import com.example.capstoneproject.entity.EducationOfCv;
import com.example.capstoneproject.entity.Experience;
import com.example.capstoneproject.entity.ExperienceOfCv;
import com.example.capstoneproject.enums.CvStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExperienceOfCvRepository extends JpaRepository<ExperienceOfCv, Integer> {
    ExperienceOfCv findByExperience_IdAndCv_Id(Integer experienceId, Integer cvId);
    @Query("SELECT s FROM ExperienceOfCv s WHERE s.cv.id = :cvId AND s.experience.Status = :status")
    List<ExperienceOfCv> findActiveExperiencesByCvId(@Param("cvId") int cvId, @Param("status") CvStatus status);

    @Query("SELECT s.experience FROM ExperienceOfCv s WHERE s.cv.id = :cvId")
    List<Experience> findExperiencesByCvId(Integer cvId);
}
