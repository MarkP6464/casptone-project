package com.example.capstoneproject.repository;

import com.example.capstoneproject.Dto.ReviewRatingViewDto;
import com.example.capstoneproject.entity.ReviewRating;
import com.example.capstoneproject.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRatingRepository extends JpaRepository<ReviewRating, Integer> {
    List<ReviewRating> findAllByExpertId(Integer expertId);

    Optional<ReviewRating> findByIdAndUser(Integer ratingId, Users user);
}
