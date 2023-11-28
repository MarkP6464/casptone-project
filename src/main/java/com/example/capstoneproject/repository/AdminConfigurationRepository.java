package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.AdminConfiguration;
import com.example.capstoneproject.entity.ApplicationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminConfigurationRepository extends JpaRepository<AdminConfiguration, Integer> {
    AdminConfiguration findByUser_Id(Integer id);
}
