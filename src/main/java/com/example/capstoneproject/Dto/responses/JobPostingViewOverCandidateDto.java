package com.example.capstoneproject.Dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class JobPostingViewOverCandidateDto {
    private Integer id;

    private String title;

    private String companyName;

    private String avatar;

    private String location;

    private String[] skill;

    private String salary;

    private String createDate;

}
