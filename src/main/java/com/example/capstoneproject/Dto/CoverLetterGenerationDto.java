package com.example.capstoneproject.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class CoverLetterGenerationDto {
    private Float temperature;

    private String job_title;

    private String company;

    private String job_description;

    private String dear;

    private LocalDate date;

}
