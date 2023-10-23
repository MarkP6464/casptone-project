package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.ReviewRequestDto;
import com.example.capstoneproject.entity.ReviewRequest;
import org.springframework.stereotype.Component;

@Component
public class ReviewRequestMapper extends AbstractMapper<ReviewRequest, ReviewRequestDto> {
    public ReviewRequestMapper() {
        super(ReviewRequest.class, ReviewRequestDto.class);
    }
}
