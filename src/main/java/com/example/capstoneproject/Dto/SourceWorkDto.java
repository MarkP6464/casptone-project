package com.example.capstoneproject.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor

@Getter
@Setter
public class SourceWorkDto {
    private Integer id;

    private Boolean isDisplay = true;
    private String Name;

    private String CourseLocation;

    private int EndYear;

    private String Skill;

    private String Description;
}
