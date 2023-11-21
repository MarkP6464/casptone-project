package com.example.capstoneproject.Dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class ExpertConfigViewDto {
    private String avatar;

    private String name;

    private String jobTitle;

    private String company;

    private String about;

    private Integer experiences;

    private String cv;

    private Double price;
}
