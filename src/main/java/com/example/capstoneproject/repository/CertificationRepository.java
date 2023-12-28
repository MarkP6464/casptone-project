package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Certification;
import com.example.capstoneproject.enums.BasicStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Integer> {
    @Query("SELECT c FROM Certification c WHERE c.cv.id = :UsersId AND c.Status = :status")
    List<Certification> findCertificationsByStatus(@Param("UsersId") int UsersId,@Param("status") BasicStatus status);

    boolean existsByIdAndCv_Id(Integer educationId, Integer cvId);

}
