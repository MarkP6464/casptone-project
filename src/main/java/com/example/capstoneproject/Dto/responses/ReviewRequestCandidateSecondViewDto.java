package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.enums.StatusReview;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
public class ReviewRequestCandidateSecondViewDto {
    private Integer id;

    private String resumeName;

    private String expert;

    private String note;

    private String price;

    private StatusReview status;

    private Timestamp receivedDate;

    private Timestamp deadline;
}
