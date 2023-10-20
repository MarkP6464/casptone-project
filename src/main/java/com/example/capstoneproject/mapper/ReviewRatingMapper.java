package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.ReviewRatingViewDto;
import com.example.capstoneproject.entity.ReviewRating;
import org.springframework.stereotype.Component;

@Component
public class ReviewRatingMapper extends AbstractMapper<ReviewRating, ReviewRatingViewDto> {
    public ReviewRatingMapper() {
        super(ReviewRating.class, ReviewRatingViewDto.class);
    }
}
