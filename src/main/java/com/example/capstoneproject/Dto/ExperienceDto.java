package com.example.capstoneproject.Dto;

import com.example.capstoneproject.enums.BasicStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class ExperienceDto {
    private Integer id;
    private String Role;
    private Integer theOrder;
    private Boolean isDisplay = true;
    private String CompanyName;
    private String duration;
    private String Location;
    private String Description;
    private BasicStatus Status;
}
