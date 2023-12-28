package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Experience;
import com.example.capstoneproject.entity.Involvement;
import com.example.capstoneproject.enums.BasicStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvolvementRepository extends JpaRepository<Involvement, Integer> {
    @Query("SELECT c FROM Involvement c WHERE c.cv.user.id = :cvId AND c.Status = :status")
    List<Involvement> findInvolvementsByStatus(@Param("cvId") int id, @Param("status") BasicStatus status);

    boolean existsByIdAndCv_User_Id(Integer involvementId, Integer UserId);

    @Query("SELECT c FROM Involvement c WHERE c.cv.user.id = :userId AND c.Status = :status ORDER BY c.id DESC")
    List<Involvement> findExperiencesByStatusOrderedByStartDateDesc(@Param("userId") Integer userId, @Param("status") BasicStatus status);

}
