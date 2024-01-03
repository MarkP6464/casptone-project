package com.example.capstoneproject.Dto;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class CoverLetterUpdateDto {
    private String dear;
    private LocalDate date;
    private String company;
    private String description;
    private String jobTitle;
    private String jobDescription;
    private Integer cvId;
    private Integer jobPostingId;
}
