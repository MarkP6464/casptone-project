package com.example.capstoneproject.Dto.responses;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private ResultDto resultDto;
}
