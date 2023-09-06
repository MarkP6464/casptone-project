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
public class SourceWorkViewDto {
    private int id;

    private String Name;

    private String CourseLocation;

    private Date EndDate;

    private String Skill;

    private String Applied;

    private CvStatus Status;
}
