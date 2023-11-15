package com.example.capstoneproject.repository;

import com.example.capstoneproject.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByUser_IdAndJobPosting_Id(Integer userId, Integer jobId);

    List<Like> findAllByUser_Id(Integer userId);
}
