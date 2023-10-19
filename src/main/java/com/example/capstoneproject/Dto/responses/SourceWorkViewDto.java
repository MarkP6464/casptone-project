package com.example.capstoneproject.Dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor

@Getter
@Setter
public class SourceWorkViewDto {
    private int id;

    private String Name;

    private String CourseLocation;

    private Boolean isDisplay;

    private int EndYear;

    private String Skill;

    private String Description;

}
