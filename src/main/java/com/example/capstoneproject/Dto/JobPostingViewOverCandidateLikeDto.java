package com.example.capstoneproject.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@NoArgsConstructor
@Getter
@Setter
public class JobPostingViewOverCandidateLikeDto {
    private Integer id;

    private String title;

    private String companyName;

    private String avatar;

    private String location;

    private String[] skill;

    private String salary;

    private String createDate;

    private boolean liked;
}
