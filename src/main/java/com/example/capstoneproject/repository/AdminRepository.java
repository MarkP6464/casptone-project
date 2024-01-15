package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    @Query(value = "SELECT * FROM capstone.users where role_id = '4'", nativeQuery = true)
    List<Admin> findAdmin();
}
