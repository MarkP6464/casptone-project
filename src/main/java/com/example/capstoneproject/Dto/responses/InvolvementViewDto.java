package com.example.capstoneproject.Dto.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@NoArgsConstructor

@Getter
@Setter
public class InvolvementViewDto {
    private int id;

    private Boolean isDisplay;

    private String OrganizationRole;

    private String OrganizationName;

    private Date StartDate;

    private Date EndDate;

    private String College;

    private String Description;
}
