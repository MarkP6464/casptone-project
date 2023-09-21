package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Involvement;
import com.example.capstoneproject.enums.BasicStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvolvementRepository extends JpaRepository<Involvement, Integer> {
    @Query("SELECT c FROM Involvement c WHERE c.user.id = :cvId AND c.Status = :status")
    List<Involvement> findInvolvementsByStatus(@Param("cvId") int id, @Param("status") BasicStatus status);

    boolean existsByIdAndUser_Id(Integer involvementId, Integer UserId);

}
