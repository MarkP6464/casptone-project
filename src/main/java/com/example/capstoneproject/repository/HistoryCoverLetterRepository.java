package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.History;
import com.example.capstoneproject.entity.HistoryCoverLetter;
import com.example.capstoneproject.enums.BasicStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryCoverLetterRepository extends JpaRepository<HistoryCoverLetter, Integer> {

    Optional<HistoryCoverLetter> findById(Integer historyId);

    List<HistoryCoverLetter> findAllByIdIn(Collection<Integer> id);
}
