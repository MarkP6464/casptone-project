package com.example.capstoneproject.Dto;

import com.example.capstoneproject.enums.StatusReview;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class JobPostingDto {

    private String title;

    private StatusReview status;

    private String workingType;

    private String companyName;

    private String avatar;

    private String about;

    private String benefit;

    private String skill;

    private String location;

    private String description;

    private String requirement;

    private Integer applyAgain;

    private String salary;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate deadline;

}
