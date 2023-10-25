package com.example.capstoneproject.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor

@Getter
@Setter
public class ExperienceDto {
    private Integer id;
    private String Role;
    private Boolean isDisplay;
    private String CompanyName;
    private String duration;
    private String Location;
    private String Description;
}
