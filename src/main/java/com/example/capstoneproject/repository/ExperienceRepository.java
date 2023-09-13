package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Certification;
import com.example.capstoneproject.entity.Education;
import com.example.capstoneproject.entity.Experience;
import com.example.capstoneproject.enums.CvStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Integer> {
    @Query("SELECT c FROM Experience c WHERE c.customer.id = :customerId AND c.Status = :status")
    List<Experience> findExperiencesByStatus(@Param("customerId") int customerId, @Param("status") CvStatus status);

    boolean existsByIdAndCustomer_Id(Integer experienceId, Integer customerId);

}
