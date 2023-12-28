package com.example.capstoneproject.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@NoArgsConstructor

@Getter
@Setter
public class EducationDto {
    private Integer id;

    private Boolean isDisplay = true;

    private Integer theOrder;

    private String Degree;

    @NotNull
    private String CollegeName;
    private String Location;

    private Integer EndYear;

    private String Minor;

    private Double Gpa;

    private String Description;
}
