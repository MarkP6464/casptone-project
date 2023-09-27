package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.enums.BasicStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CvRepository extends JpaRepository<Cv, Integer> {
    @Query("SELECT c FROM Cv c WHERE c.user.id = :UsersId AND c.Status = :status")
    List<Cv> findAllByUsersIdAndStatus(@Param("UsersId") int id, @Param("status") BasicStatus status);

    @Query("SELECT c FROM Cv c WHERE c.user.id =:UsersId AND c.id = :cvId AND c.Status = :status")
    Cv findCvByIdAndStatus(@Param("UsersId") int UsersId, @Param("cvId") int id, @Param("status") BasicStatus status);

    Optional<Cv> findByIdAndUserId(Integer id, Integer UsersId);

    @Query("SELECT c FROM Cv c WHERE c.id =:cvId AND c.id = :cvId AND c.Status = :status")
    Cv findCvById(@Param("cvId") int cvId, @Param("status") BasicStatus status);
}
