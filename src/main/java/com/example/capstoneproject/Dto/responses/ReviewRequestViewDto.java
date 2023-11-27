package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.enums.ReviewStatus;
import com.example.capstoneproject.enums.StatusReview;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class ReviewRequestViewDto {
    private Integer id;

    private String resumeName;

    private String avatar;

    private String name;

    private String note;

    private Long price;

    private StatusReview status;

    private Timestamp receivedDate;

    private Timestamp deadline;

}
