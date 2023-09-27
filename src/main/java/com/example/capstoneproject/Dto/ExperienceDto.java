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
    private Integer id;
    private String Role;
    private Boolean isDisplay;
    private String CompanyName;
    private Date StartDate;
    private Date EndDate;
    private String Location;
    private String Description;
}
