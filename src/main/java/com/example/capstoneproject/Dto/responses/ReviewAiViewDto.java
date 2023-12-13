package com.example.capstoneproject.Dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAiViewDto {
    private Integer id;

    private String version;

    private String review;

    private Timestamp timestamp;
}
