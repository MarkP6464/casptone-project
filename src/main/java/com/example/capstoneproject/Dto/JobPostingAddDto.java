package com.example.capstoneproject.Dto;

import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.StatusReview;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class JobPostingAddDto {

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

    private Integer applyAgain;

    private String[] skill;

    private Boolean ban;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;

}
