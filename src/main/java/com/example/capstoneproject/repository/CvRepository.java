package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.enums.CvStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CvRepository extends JpaRepository<Cv, Integer> {
    @Query("SELECT c FROM Cv c WHERE c.customer.id = :customerId AND c.Status = :status")
    List<Cv> findAllByCustomerIdAndStatus(@Param("customerId") int id, @Param("status") CvStatus status);

    @Query("SELECT c FROM Cv c WHERE c.id = :cvId AND c.Status = :status")
    Cv findCvByIdAndStatus(@Param("cvId") int id, @Param("status") CvStatus status);
}
