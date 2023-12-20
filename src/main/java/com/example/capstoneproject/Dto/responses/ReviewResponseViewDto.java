package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.Dto.CvBodyReviewDto;
import com.example.capstoneproject.Dto.UsersDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class ReviewResponseViewDto {
    private Integer id;

    private String overall;

    private CvBodyReviewDto feedbackDetail;

    private Double score;

    private String comment;

    private LocalDate dateComment;

    private ReviewRequestViewDto request;

    private UsersResponseViewDto user;
}
