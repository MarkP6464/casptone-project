package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.ReviewResponseDto;
import com.example.capstoneproject.entity.ReviewResponse;
import org.springframework.stereotype.Component;

@Component
public class ReviewResponseMapper extends AbstractMapper<ReviewResponse, ReviewResponseDto> {
    public ReviewResponseMapper() {
        super(ReviewResponse.class, ReviewResponseDto.class);
    }
}
