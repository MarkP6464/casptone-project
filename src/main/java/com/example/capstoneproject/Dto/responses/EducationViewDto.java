package com.example.capstoneproject.Dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor

@Getter
@Setter
public class EducationViewDto {
    private Integer id;

    private Boolean isDisplay = true;

    private String Degree;

    private String CollegeName;

    private String Location;

    private Integer EndYear;

    private String Minor;

    private double Gpa;

    private String Description;
}
