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

}
