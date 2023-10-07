package com.example.capstoneproject.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@NoArgsConstructor

@Getter
@Setter
public class InvolvementDto {
    private Integer id;

    private String OrganizationRole;

    private Boolean isDisplay;
    private String OrganizationName;


    private Date StartDate;

    private Date EndDate;

    private String College;

    private String Description;
}
