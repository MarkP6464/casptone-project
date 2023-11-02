package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.Cv;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class HistoryDto {
    private Integer id;

    private CvBodyReviewDto cvBody;

    private Timestamp timestamp;
}
