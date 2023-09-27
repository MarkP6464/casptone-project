package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findById(String email);

    Optional<Users> findUsersById(Integer Id);
}
