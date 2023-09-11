package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Certification;
import com.example.capstoneproject.entity.Experience;
import com.example.capstoneproject.entity.Involvement;
import com.example.capstoneproject.enums.CvStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvolvementRepository extends JpaRepository<Involvement, Integer> {
    @Query("SELECT c FROM Involvement c WHERE c.customer.id = :cvId AND c.Status = :status")
    List<Involvement> findInvolvementsByStatus(@Param("cvId") int id, @Param("status") CvStatus status);

    boolean existsByIdAndCustomer_Id(Integer involvementId, Integer cvId);

    @Query("SELECT c FROM Involvement c WHERE c.id = :involvementId AND c.Status = :status")
    Involvement findInvolvementById(@Param("involvementId") int involvementId, @Param("status") CvStatus status);

    @Query("SELECT c FROM Involvement c WHERE c.Status = :status")
    List<Involvement> findByStatus(@Param("status") CvStatus status);
}
