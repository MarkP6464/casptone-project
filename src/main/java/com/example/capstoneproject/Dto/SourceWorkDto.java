package com.example.capstoneproject.Dto;

import lombok.*;

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
