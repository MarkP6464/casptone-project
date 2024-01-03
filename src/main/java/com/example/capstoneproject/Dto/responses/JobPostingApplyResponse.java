package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.Dto.CoverLetterDto;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.StatusReview;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class JobPostingApplyResponse {
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

    private LocalDate deadline;

    private LocalDateTime createDate;

    private LocalDate updateDate;

    private BasicStatus status;

    private Integer apply;

    private StatusReview share;

    private List<HistoryCoverLetterResponse> historyCoverLetters;

}
