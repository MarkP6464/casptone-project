package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Evaluate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluateRepository extends JpaRepository<Evaluate, Integer> {

    Evaluate findById(int Id);
    List<Evaluate> findAll();
    List<Evaluate> findByTitleContainingIgnoreCase(String search);

}
