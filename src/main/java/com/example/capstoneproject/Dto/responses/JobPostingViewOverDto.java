package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.enums.StatusReview;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class JobPostingViewOverDto {
    private Integer id;

    private String title;

    private StatusReview status;

    private Integer view;

    private Integer application;

    private LocalDateTime timestamp;
}
