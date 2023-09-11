package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Skill;
import com.example.capstoneproject.entity.SkillOfCv;
import com.example.capstoneproject.enums.CvStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SkillOfCvRepository extends JpaRepository<SkillOfCv, Integer> {
    SkillOfCv findBySkill_IdAndCv_Id(Integer skillId, Integer cvId);
    @Query("SELECT s FROM SkillOfCv s WHERE s.cv.id = :cvId AND s.skill.Status = :status")
    List<SkillOfCv> findActiveSkillsByCvId(@Param("cvId") int cvId, @Param("status") CvStatus status);

    @Query("SELECT s.skill FROM SkillOfCv s WHERE s.cv.id = :cvId")
    List<Skill> findSkillsByCvId(Integer cvId);
}
