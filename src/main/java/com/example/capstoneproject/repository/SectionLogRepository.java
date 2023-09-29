package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.SectionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import javax.transaction.Transactional;

public interface SectionLogRepository extends JpaRepository<SectionLog, Integer> {

    @Transactional
    void deleteBySection_Id(int sectionId);
}
