package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Certification;
import com.example.capstoneproject.entity.Education;
import com.example.capstoneproject.enums.CvStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EducationRepository extends JpaRepository<Education, Integer> {
    @Query("SELECT c FROM Education c WHERE c.customer.id = :customerId AND c.Status = :status")
    List<Education> findEducationsByStatus(@Param("customerId") int customerId,@Param("status") CvStatus status);

    boolean existsByIdAndCustomer_Id(Integer educationId, Integer customerId);

    @Query("SELECT c FROM Education c WHERE c.id = :educationId AND c.Status = :status")
    Education findEducationById(@Param("educationId") int educationId, @Param("status") CvStatus status);

    @Query("SELECT c FROM Education c WHERE c.Status = :status")
    List<Education> findByStatus(@Param("status") CvStatus status);
}
