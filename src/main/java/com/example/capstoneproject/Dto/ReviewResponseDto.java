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
    private String overall;

    private String feedbackDetail;

    private ReviewStatus status;

    private ReviewRequest reviewRequest;
}
