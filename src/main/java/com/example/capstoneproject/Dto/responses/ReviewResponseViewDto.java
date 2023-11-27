package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.Dto.CvBodyReviewDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReviewResponseViewDto {
    private Integer id;

    private String overall;

    private CvBodyReviewDto feedbackDetail;

    private Double score;

    private String comment;

    private ReviewRequestViewDto request;
}
