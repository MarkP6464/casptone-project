package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.CoverLetter;
import com.example.capstoneproject.enums.BasicStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CoverLetterRepository extends JpaRepository<CoverLetter, Integer> {
    Optional<CoverLetter> findByIdAndStatus(int coverLetterId, BasicStatus status);

    List<CoverLetter> findByCv_User_IdAndStatus(Integer userId, BasicStatus status);

    boolean existsByCv_User_IdAndIdAndStatus(Integer UserId, Integer coverLetterId, BasicStatus status);

    Optional<CoverLetter> findByCv_User_IdAndIdAndStatus(Integer UserId, Integer coverLetterId, BasicStatus status);

    List<CoverLetter> findByCv_User_IdAndCv_IdAndStatus(Integer userId, Integer cvId, BasicStatus status);
}
