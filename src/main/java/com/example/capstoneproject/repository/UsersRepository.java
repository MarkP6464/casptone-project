package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {

    Optional<Users> findByEmail(String email);

    List<Users> findAllByStatusAndIdNot(BasicStatus status, Integer adminId);

    Optional<Users> findUsersById(Integer Id);

    Optional<Users> findByIdAndRole_RoleName(Integer userId, RoleType roleType);

}
