package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Expert;
import com.example.capstoneproject.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ExpertRepository extends JpaRepository<Expert, Integer> {
    @Query("SELECT e FROM Expert e WHERE e.id = :expertId AND e.role.roleName = :roleName")
    Optional<Expert> findByIdAndRoleName(
            @Param("expertId") Integer expertId,
            @Param("roleName") String roleName
    );

    Optional<Expert> findByIdAndRole_RoleName(Integer expertId, RoleType roleName);

    List<Expert> findByRole_RoleName(RoleType roleName);

//    @Query(nativeQuery = true, value = "SELECT app FROM #{#entityName} AS app WHERE app.status=:#{#status.name()}")
//    List<Application> find(@Param("status") ApplicationStatus status);

}
