package com.example.capstoneproject.Dto;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InvolvementDto {
    private String OrganizationRole;

    private String OrganizationName;

    private Date StartDate;

    private Date EndDate;

    private String College;

    private String Description;
}
