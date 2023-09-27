package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.SourceWork;
import com.example.capstoneproject.enums.BasicStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SourceWorkRepository extends JpaRepository<SourceWork, Integer> {
    @Query("SELECT c FROM SourceWork c WHERE c.user.id = :cvId AND c.Status = :status")
    List<SourceWork> findSourceWorkByCv_IdAndStatus(@Param("cvId") int cvId, @Param("status") BasicStatus status);

    boolean existsByIdAndUser_Id(Integer sourceWorkId, Integer UserId);
}
