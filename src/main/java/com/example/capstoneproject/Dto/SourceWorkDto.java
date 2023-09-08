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
public class SourceWorkDto {

    private String Name;

    private String CourseLocation;

    private int EndYear;

    private String Skill;

    private String Description;
}
