package com.example.capstoneproject.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@NoArgsConstructor

@Getter
@Setter
public class ExperienceDto {
    private Integer id;
    private String Role;
    private Boolean isDisplay;
    private String CompanyName;
    private Date StartDate;
    private Date EndDate;
    private String Location;
    private String Description;
}
