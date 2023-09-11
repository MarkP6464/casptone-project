package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Certification;
import com.example.capstoneproject.entity.CertificationOfCv;
import com.example.capstoneproject.entity.Education;
import com.example.capstoneproject.entity.EducationOfCv;
import com.example.capstoneproject.enums.CvStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EducationOfCvRepository extends JpaRepository<EducationOfCv, Integer> {
    EducationOfCv findByEducation_IdAndCv_Id(Integer educationId, Integer cvId);
    @Query("SELECT s FROM EducationOfCv s WHERE s.cv.id = :cvId AND s.education.Status = :status")
    List<EducationOfCv> findActiveEducationsByCvId(@Param("cvId") int cvId, @Param("status") CvStatus status);

    @Query("SELECT s.education FROM EducationOfCv s WHERE s.cv.id = :cvId")
    List<Education> findEducationsByCvId(Integer cvId);
}
