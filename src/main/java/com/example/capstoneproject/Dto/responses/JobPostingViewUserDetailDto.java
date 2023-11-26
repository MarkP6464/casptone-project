package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.StatusReview;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@NoArgsConstructor
@Getter
@Setter
public class JobPostingViewUserDetailDto {
    private Integer id;

    private String title;

    private String workingType;

    private String companyName;

    private String avatar;

    private String location;

    private String about;

    private String benefit;

    private String description;

    private String requirement;

    private String salary;

    private String[] skill;

    private Integer view;

    private boolean liked;

    private LocalDate deadline;

    private LocalDate createDate;

    private LocalDate updateDate;

    private BasicStatus status;

    private StatusReview share;
}
