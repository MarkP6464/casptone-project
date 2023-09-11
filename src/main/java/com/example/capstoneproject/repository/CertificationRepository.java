package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Certification;
import com.example.capstoneproject.entity.Skill;
import com.example.capstoneproject.enums.CvStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Integer> {
    @Query("SELECT c FROM Certification c WHERE c.customer.id = :customerId AND c.Status = :status")
    List<Certification> findCertificationsByStatus(@Param("customerId") int customerId,@Param("status") CvStatus status);

    boolean existsByIdAndCustomer_Id(Integer certificationId, Integer customerId);

    @Query("SELECT c FROM Certification c WHERE c.id = :certificationId AND c.Status = :status")
    Certification findCertificationById(@Param("certificationId") int certificationId, @Param("status") CvStatus status);

    @Query("SELECT c FROM Certification c WHERE c.Status = :status")
    List<Certification> findByStatus(@Param("status") CvStatus status);
}
