package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Certification;
import com.example.capstoneproject.entity.CertificationOfCv;
import com.example.capstoneproject.enums.CvStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CertificationOfCvRepository extends JpaRepository<CertificationOfCv, Integer> {
    CertificationOfCv findByCertification_IdAndCv_Id(Integer certificationId, Integer cvId);
    @Query("SELECT s FROM CertificationOfCv s WHERE s.cv.id = :cvId AND s.certification.Status = :status")
    List<CertificationOfCv> findActiveCertificationsByCvId(@Param("cvId") int cvId, @Param("status") CvStatus status);

    @Query("SELECT s.certification FROM CertificationOfCv s WHERE s.cv.id = :cvId")
    List<Certification> findCertificationsByCvId(Integer cvId);
}
