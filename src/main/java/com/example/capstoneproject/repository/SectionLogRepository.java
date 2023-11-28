package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.SectionLog;
import com.example.capstoneproject.enums.SectionEvaluate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface SectionLogRepository extends JpaRepository<SectionLog, Integer> {

    @Transactional
    void deleteBySection_Id(int sectionId);

    @Query("SELECT sl FROM SectionLog sl " +
            "JOIN sl.section s " +
            "WHERE s.TypeName = :typeName AND s.TypeId = :typeId")
    List<SectionLog> findBySection_TypeNameAndSection_TypeId(@Param("typeName") SectionEvaluate typeName, @Param("typeId") Integer typeId);

}
