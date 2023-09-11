package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Experience;
import com.example.capstoneproject.entity.ExperienceOfCv;
import com.example.capstoneproject.entity.Involvement;
import com.example.capstoneproject.entity.InvolvementOfCv;
import com.example.capstoneproject.enums.CvStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvolvementOfCvRepository extends JpaRepository<InvolvementOfCv, Integer> {
    InvolvementOfCv findByInvolvement_IdAndCv_Id(Integer involvementId, Integer cvId);
    @Query("SELECT s FROM InvolvementOfCv s WHERE s.cv.id = :cvId AND s.involvement.Status = :status")
    List<InvolvementOfCv> findActiveInvolvementsByCvId(@Param("cvId") int cvId, @Param("status") CvStatus status);

    @Query("SELECT s.involvement FROM InvolvementOfCv s WHERE s.cv.id = :cvId")
    List<Involvement> findInvolvementsByCvId(Integer cvId);
}
