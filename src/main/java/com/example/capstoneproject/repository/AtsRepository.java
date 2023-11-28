package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Ats;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface AtsRepository extends JpaRepository<Ats, Integer> {
    List<Ats> findAllByJobDescriptionId(Integer jobDescriptionId);

    @Transactional
    void deleteByJobDescriptionId(Integer jobDescriptionId);
}
