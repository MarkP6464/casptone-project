package com.example.capstoneproject.Dto;

import com.example.capstoneproject.Dto.responses.JobPostingApplyResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
public class HistoryDto {
    private Integer id;

    private CvBodyReviewDto cvBody;

    private Timestamp timestamp;

    private Integer cvId;

    private JobPostingApplyResponse jobPosting;


}
