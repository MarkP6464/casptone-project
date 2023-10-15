package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.ReviewRatingDto;
import com.example.capstoneproject.entity.Expert;
import com.example.capstoneproject.entity.ReviewRating;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.mapper.ReviewRatingMapper;
import com.example.capstoneproject.repository.ExpertRepository;
import com.example.capstoneproject.repository.ReviewRatingRepository;
import com.example.capstoneproject.repository.UsersRepository;
import com.example.capstoneproject.service.ReviewRatingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class ReviewRatingServiceImpl implements ReviewRatingService {

    @Autowired
    ReviewRatingRepository reviewRatingRepository;

    @Autowired
    ReviewRatingMapper reviewRatingMapper;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    ExpertRepository expertRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public boolean updateReviewRating(Integer userId, Integer ratingId, ReviewRatingDto dto) {
        Optional<Users> usersOptional = usersRepository.findByUserIdAndRoleName(userId, RoleType.USER);
        Date currentDate = Calendar.getInstance().getTime();
        if(usersOptional.isPresent()){
            Users customer = usersOptional.get();
            Optional<ReviewRating> reviewRatingOptional = reviewRatingRepository.findByIdAndUser(ratingId,customer);
            if (reviewRatingOptional.isPresent()){
                ReviewRating reviewRating = reviewRatingOptional.get();
                reviewRating.setDateComment(currentDate);
                if (dto.getComment() != null && !dto.getComment().equals(reviewRating.getComment())) {
                    reviewRating.setComment(dto.getComment());
                }
                if (dto.getScore() != reviewRating.getScore()){
                    reviewRating.setScore(dto.getScore());
                }
                reviewRatingRepository.save(reviewRating);
                return true;
            }else {
                throw new RuntimeException("Rating ID not found.");
            }
        }else {
            throw new RuntimeException("User ID not found.");
        }
    }

    @Override
    public ReviewRatingDto createReviewRating(Integer userId, Integer expertId, ReviewRatingDto dto) {
        Optional<Expert> expertOptional = expertRepository.findByIdAndRole_RoleName(expertId, RoleType.EXPERT);
        Optional<Users> usersOptional = usersRepository.findByIdAndRole_RoleName(userId, RoleType.USER);
        Date currentDate = Calendar.getInstance().getTime();
        ReviewRating reviewRating = new ReviewRating();
        if(usersOptional.isPresent() && expertOptional.isPresent()){
            Users customer = usersOptional.get();
            Expert expert = expertOptional.get();
            reviewRating.setUser(customer);
            reviewRating.setExpert(expert);
            reviewRating.setScore(dto.getScore());
            reviewRating.setComment(dto.getComment());
            reviewRating.setDateComment(currentDate);
            reviewRatingRepository.save(reviewRating);
        }else{
            throw new RuntimeException("User ID or Expert ID not found.");
        }

        return modelMapper.map(reviewRating, ReviewRatingDto.class);
    }

    @Override
    public boolean deleteReviewRating(Integer userId, Integer ratingId) {
        Optional<Users> usersOptional = usersRepository.findByUserIdAndRoleName(userId, RoleType.USER);
        if(usersOptional.isPresent()){
            Users customer = usersOptional.get();
            Optional<ReviewRating> reviewRatingOptional = reviewRatingRepository.findByIdAndUser(ratingId,customer);
            if (reviewRatingOptional.isPresent()){
                ReviewRating reviewRating = reviewRatingOptional.get();
                reviewRatingRepository.delete(reviewRating);
                return true;
            }else {
                throw new RuntimeException("Rating ID not found.");
            }
        }else {
            throw new RuntimeException("User ID not found.");
        }
    }
}
