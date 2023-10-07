package com.example.capstoneproject.Dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@NoArgsConstructor

@Getter
@Setter
public class ExperienceViewDto {
    private int id;

    private Boolean isDisplay;

    private String Role;

    private String CompanyName;

    private Date StartDate;

    private Date EndDate;

    private String Location;

    private String Description;
}
