package com.example.capstoneproject.Dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewResponseUpdateDto {
    private String overall;
    private CvBodyReviewDto cv;
}
