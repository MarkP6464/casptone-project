package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Ats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AtsRepository extends JpaRepository<Ats, Integer> {
    List<Ats> findAllByJobDescriptionId(Integer jobDescriptionId);
    void deleteByJobDescriptionId(Integer jobDescriptionId);
}
