package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.History;
import com.example.capstoneproject.enums.BasicStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {
    List<History> findAllByCv_IdAndCv_StatusOrderByTimestampDesc(Integer cvId, BasicStatus status);

    Optional<History> findById(Integer historyId);

    Optional<History> findByCv_IdAndCv_StatusAndId(Integer cvId, BasicStatus status, Integer historyId);
    List<History> findAllByIdIn(Collection<Integer> id);
}
