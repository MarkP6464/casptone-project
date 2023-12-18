package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.Section;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.SectionEvaluate;
import com.example.capstoneproject.enums.SectionLogStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
@Repository
public interface CvRepository extends JpaRepository<Cv, Integer> {

    @Query("SELECT c FROM Cv c WHERE c.user.id =:UsersId AND c.id = :cvId AND c.status = :status")
    Cv findCvByIdAndStatus(@Param("UsersId") int UsersId, @Param("cvId") int id, @Param("status") BasicStatus status);

    Optional<Cv> findByIdAndUserId(Integer id, Integer UsersId);

    Optional<Cv> findByUser_IdAndId(Integer UsersId, Integer cvId);

    List<Cv> findAllByUser_IdAndStatusAndSearchableIsTrue(Integer candidateId, BasicStatus status);

    @Query("SELECT c FROM Cv c WHERE c.id =:cvId AND c.id = :cvId AND c.status = :status")
    Optional<Cv> findByIdAndStatus(@Param("cvId") Integer cvId, @Param("status") BasicStatus status);

    @Query("SELECT c FROM Cv c WHERE c.id =:cvId AND c.id = :cvId AND c.status = :status")
    Cv findCvById(@Param("cvId") int cvId, @Param("status") BasicStatus status);

    @Query("SELECT DISTINCT s FROM Section s " +
            "INNER JOIN s.sectionLogs sl " +
            "WHERE sl.evaluate.id = :evaluateId AND sl.Status != :status")
    List<Section> findSectionsWithNonPassStatus(@Param("evaluateId") int evaluateId, @Param("status") SectionLogStatus status);

    @Query("SELECT DISTINCT s FROM Section s " +
            "INNER JOIN s.sectionLogs sl " +
            "WHERE sl.evaluate.id = :evaluateId AND sl.Status = :status")
    List<Section> findSectionsWithPassStatus(@Param("evaluateId") int evaluateId, @Param("status") SectionLogStatus status);

    @Query("SELECT DISTINCT s FROM Section s " +
            "INNER JOIN s.sectionLogs sl " +
            "WHERE sl.evaluate.id = :evaluateId")
    List<Section> findSections(@Param("evaluateId") int evaluateId);

    @Query("SELECT s FROM Section s WHERE s.TypeName = :typeName")
    List<Section> findAllByTypeName(SectionEvaluate typeName);

    @Query("SELECT s FROM Section s WHERE s.TypeName IN :typeNames")
    List<Section> findAllByTypeNames(@Param("typeNames") List<SectionEvaluate> typeNames);

    @Query("SELECT c FROM Cv c WHERE c.user.id = :UsersId AND c.status = :status")
    List<Cv> findAllByUsersIdAndStatus(@Param("UsersId") int id, @Param("status") BasicStatus status);

    List<Cv> findAllByStatusAndSearchable(BasicStatus status, boolean searchable);

    List<Cv> findAllByIdIn(Collection<Integer> id);

    List<Cv> findAllByUser_Id(Integer userId);

    List<Cv> findAllByUser_IdAndIdIsNot(Integer userId, Integer cvId);

    List<Cv> findAllByUser_IdAndStatus(Integer userId, BasicStatus status);
}
