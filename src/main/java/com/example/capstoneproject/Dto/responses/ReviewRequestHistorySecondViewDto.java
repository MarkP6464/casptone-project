package com.example.capstoneproject.Dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
@NoArgsConstructor
@Getter
@Setter
public class ReviewRequestHistorySecondViewDto {
    private Integer id;

    private String resumeName;

    private String candidate;

    private String price;

    private Double star;

    private String response;

    private Timestamp receivedDate;
}
