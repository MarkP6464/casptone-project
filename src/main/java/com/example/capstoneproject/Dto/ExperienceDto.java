package com.example.capstoneproject.Dto;

import lombok.*;

import java.util.Date;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExperienceDto {
    private int id;

    private String Role;

    private String CompanyName;
    private Boolean isDisplay;


    private Date StartDate;

    private Date EndDate;

    private String Location;

    private String Description;
}
