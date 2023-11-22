package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Expert;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ExpertRepository extends JpaRepository<Expert, Integer> {

//    @Query("SELECT e FROM Expert e JOIN Users u ON e.id = u.id JOIN u.role r WHERE u.id = :userId AND r.roleName = :roleName")
    @Query("SELECT e FROM Users e JOIN e.role r WHERE e.id = :userId AND r.roleName = :roleName")
    Optional<Expert> findByIdAndRole_RoleName(@Param("userId") Integer userId, @Param("roleName") RoleType roleName);

    @Query("SELECT e FROM Users e JOIN e.role r WHERE e.id = :expertId AND r.roleName = :roleName")
    Users findExpertByIdAndRole_RoleName(@Param("expertId") Integer expertId, @Param("roleName") RoleType roleName);


    List<Expert> findAllByRole_RoleNameAndPunishFalse(RoleType roleName);

    List<Expert> findByPunishIsTrue();

}
