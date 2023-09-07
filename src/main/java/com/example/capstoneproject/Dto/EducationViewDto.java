package com.example.capstoneproject.Dto;

import com.example.capstoneproject.enums.CvStatus;
import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EducationViewDto {
    private int id;

    private String Degree;

    private String NameCollege;

    private String Location;

    private Date EndDate;

    private String Minor;

    private double Gpa;

    private String Description;

    private CvStatus Status;
}