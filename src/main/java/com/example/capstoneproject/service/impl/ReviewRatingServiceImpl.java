package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.ReviewRatingDto;
import com.example.capstoneproject.entity.Expert;
import com.example.capstoneproject.entity.ReviewRating;
import com.example.capstoneproject.entity.ReviewResponse;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.ReviewStatus;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.mapper.ReviewRatingMapper;
import com.example.capstoneproject.repository.ExpertRepository;
import com.example.capstoneproject.repository.ReviewRatingRepository;
import com.example.capstoneproject.repository.ReviewResponseRepository;
import com.example.capstoneproject.repository.UsersRepository;
import com.example.capstoneproject.service.ReviewRatingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
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
    ReviewResponseRepository reviewResponseRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public boolean updateReviewRating(Integer userId, Integer ratingId, ReviewRatingDto dto) {
        Optional<Users> usersOptional = usersRepository.findByIdAndRole_RoleName(userId, RoleType.USER);
        LocalDate currentDate = LocalDate.now();
        if(usersOptional.isPresent()){
            Users customer = usersOptional.get();
            Optional<ReviewRating> reviewRatingOptional = reviewRatingRepository.findByIdAndUserAndStatus(ratingId,customer, BasicStatus.ACTIVE);
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
    public ReviewRatingDto createReviewRating(Integer userId, Integer responseId, ReviewRatingDto dto) {
        Optional<ReviewResponse> reviewResponseOptional = reviewResponseRepository.findByIdAndStatus(responseId, ReviewStatus.SEND);
        Optional<Users> usersOptional = usersRepository.findByIdAndRole_RoleName(userId, RoleType.USER);
        Optional<ReviewRating> reviewRatingOptional = reviewRatingRepository.findByUser_IdAndReviewResponse_Id(userId,responseId);
        LocalDate currentDate = LocalDate.now();
        ReviewRating reviewRating = new ReviewRating();
        if (reviewRatingOptional.isPresent()){
            throw new RuntimeException("You commented previous for this review response.");
        }else {
            if(usersOptional.isPresent() && reviewResponseOptional.isPresent()){
                Users customer = usersOptional.get();
                ReviewResponse reviewResponse = reviewResponseOptional.get();
                if(Objects.equals(reviewResponse.getReviewRequest().getCv().getUser().getId(), customer.getId())){
                    reviewRating.setUser(customer);
                    reviewRating.setReviewResponse(reviewResponse);
                    reviewRating.setScore(dto.getScore());
                    reviewRating.setComment(dto.getComment());
                    reviewRating.setDateComment(currentDate);
                    reviewRating.setStatus(BasicStatus.ACTIVE);
                    reviewRatingRepository.save(reviewRating);
                }else{
                    throw new RuntimeException("User ID not exist in Response ID");
                }
            }else{
                throw new RuntimeException("User ID or Expert ID not found.");
            }
        }


        return modelMapper.map(reviewRating, ReviewRatingDto.class);
    }

    @Override
    public boolean deleteReviewRating(Integer userId, Integer ratingId) {
        Optional<Users> usersOptional = usersRepository.findByIdAndRole_RoleName(userId, RoleType.USER);
        if(usersOptional.isPresent()){
            Users customer = usersOptional.get();
            Optional<ReviewRating> reviewRatingOptional = reviewRatingRepository.findByIdAndUserAndStatus(ratingId,customer,BasicStatus.ACTIVE);
            if (reviewRatingOptional.isPresent()){
                ReviewRating reviewRating = reviewRatingOptional.get();
                reviewRating.setStatus(BasicStatus.DELETED);
                reviewRatingRepository.save(reviewRating);
                return true;
            }else {
                throw new RuntimeException("Rating ID not found.");
            }
        }else {
            throw new RuntimeException("User ID not found.");
        }
    }
}
