package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.enums.StatusReview;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ReviewRequestSecondViewDto {
    private Integer id;

    private String resumeName;

    private String avatar;

    private String name;

    private String note;

    private Double price;

    private StatusReview status;

    private String receivedDate;

    private String deadline;
}
