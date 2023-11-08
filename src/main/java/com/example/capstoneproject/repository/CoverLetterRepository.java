package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.CoverLetter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoverLetterRepository extends JpaRepository<CoverLetter, Integer> {
    Optional<CoverLetter> findById(int coverLetterId);

    boolean existsByCv_User_IdAndId(Integer UserId, Integer coverLetterId);

    Optional<CoverLetter> findByCv_User_IdAndId(Integer UserId, Integer coverLetterId);
}
