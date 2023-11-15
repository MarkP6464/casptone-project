package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    //Optional<Users> findById(String email);

    Optional<Users> findByEmail(String email);

    Optional<Users> findUsersById(Integer Id);

//    List<Users> findAllByRole_RoleName(RoleType roleName);
//
//    List<Users> findByNameContains(String search, RoleType roleName);
//    @Query("SELECT u FROM Users u JOIN u.role r WHERE (u.Name LIKE %:search% OR u.ex.title LIKE %:search%) AND r.roleName = :roleName")
//    List<Users> findByNameOrExpertTitleAndRole(@Param("search") String search, @Param("roleName") RoleType roleName);

//    @Query("SELECT u FROM Users u WHERE u.id = :userId")
//    Optional<Users> findByUserIdAndRoleName(
//            @Param("userId") Integer userId
//    );

    Optional<Users> findByIdAndRole_RoleName(Integer userId, RoleType roleType);



}
