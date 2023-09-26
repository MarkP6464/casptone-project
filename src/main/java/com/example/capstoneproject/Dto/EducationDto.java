package com.example.capstoneproject.Dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EducationDto {
    private int id;

    private String Degree;

    private String CollegeName;
    private Boolean isDisplay;


    private String Location;

    private int EndYear;

    private String Minor;

    private double Gpa;

    private String Description;
}
