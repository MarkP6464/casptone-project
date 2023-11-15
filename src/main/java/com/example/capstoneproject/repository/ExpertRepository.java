package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Expert;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ExpertRepository extends JpaRepository<Expert, Integer> {

    Optional<Expert> findByIdAndUsers_Role_RoleName(Integer expertId, RoleType roleName);

    List<Expert> findByUsers_Role_RoleName(RoleType roleName);

//    List<Users> findByUsers_NameOrTitleContainsAndUsers_Role_RoleName;
//
    List<Expert> findAllByUsers_Role_RoleName(RoleType roleName);




}
