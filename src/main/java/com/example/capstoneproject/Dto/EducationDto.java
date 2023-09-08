package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.enums.CvStatus;
import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EducationDto {
    private String Degree;

    private String CollegeName;

    private String Location;

    private int EndYear;

    private String Minor;

    private double Gpa;

    private String Description;
}
