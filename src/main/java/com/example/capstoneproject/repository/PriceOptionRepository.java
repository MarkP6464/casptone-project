package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.PriceOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface PriceOptionRepository extends JpaRepository<PriceOption, Integer> {
    Optional<PriceOption> findByExpertIdAndId(Integer expertId, Integer optionId);

    List<PriceOption> findAllByExpertId(Integer expertId);
    @Modifying
    @Query("DELETE FROM PriceOption p WHERE p.expert.id = :expertId")
    void deleteByExpert(@Param("expertId") Integer expertId);
}
