package com.example.capstoneproject.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor

@Getter
@Setter
public class EducationDto {
    private Integer id;

    private Boolean isDisplay = true;

    private Integer theOrder;

    private String Degree;

    private String CollegeName;
    private String Location;

    private int EndYear;

    private String Minor;

    private double Gpa;

    private String Description;
}
