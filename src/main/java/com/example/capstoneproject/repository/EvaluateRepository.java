package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Evaluate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluateRepository extends JpaRepository<Evaluate, Integer> {

    Evaluate findById(int Id);

}
