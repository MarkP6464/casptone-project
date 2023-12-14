package com.example.capstoneproject.repository;

import com.example.capstoneproject.Dto.BulletPointDto;
import com.example.capstoneproject.entity.Section;
import com.example.capstoneproject.enums.SectionEvaluate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Integer> {
    @Query("SELECT new com.example.capstoneproject.Dto.BulletPointDto(e.title, e.description, sl.Bullet,sl.count, sl.Status) " +
            "FROM Section s " +
            "JOIN s.sectionLogs sl " +
            "JOIN sl.evaluate e " +
            "WHERE s.TypeId = :typeId AND s.TypeName = :typeName")
    List<BulletPointDto> findBulletPointDtoByTypeIdAndTypeName(@Param("typeId") int typeId, @Param("typeName") SectionEvaluate typeName);

    @Query("SELECT s FROM Section s WHERE s.TypeName = :typeName AND s.TypeId = :typeId")
    Section findByTypeNameAndTypeId(@Param("typeName") SectionEvaluate typeName, @Param("typeId") int typeId);


}
