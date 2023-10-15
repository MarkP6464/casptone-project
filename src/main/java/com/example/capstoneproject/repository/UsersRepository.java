package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    //Optional<Users> findById(String email);

    Optional<Users> findUsersById(Integer Id);

    @Query("SELECT u FROM Users u WHERE u.id = :userId AND u.role.roleName = :roleName")
    Optional<Users> findByUserIdAndRoleName(
            @Param("userId") Integer userId,
            @Param("roleName") RoleType roleName
    );

    Optional<Users> findByIdAndRole_RoleName(Integer userId, RoleType roleType);



}
