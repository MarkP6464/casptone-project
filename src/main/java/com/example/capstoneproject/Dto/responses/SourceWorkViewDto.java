package com.example.capstoneproject.Dto.responses;

import lombok.*;

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

    private Boolean isDisplay;

    private int EndYear;

    private String Skill;

    private String Description;

}
