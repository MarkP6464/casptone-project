package com.example.capstoneproject.Dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
public class ReviewRequestHistoryViewDto {
    private Integer id;

    private String resumeName;

    private String candidate;

    private Long price;

    private Double star;

    private String response;

    private Timestamp receivedDate;
}
