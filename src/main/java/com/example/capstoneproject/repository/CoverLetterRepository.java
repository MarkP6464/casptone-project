package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.CoverLetter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoverLetterRepository extends JpaRepository<CoverLetter, Integer> {
    Optional<CoverLetter> findById(int coverLetterId);
    boolean existsByUser_IdAndId(Integer UserId, Integer coverLetterId);

    Optional<CoverLetter> findByUser_IdAndId(Integer UserId, Integer coverLetterId);
}
