package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.ReviewRequest;
import com.example.capstoneproject.enums.ReviewStatus;
import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewResponseDto {

    private Integer id;

    private String overall;

    private CvBodyReviewDto feedbackDetail;

    private Double score;

    private String comment;

}
