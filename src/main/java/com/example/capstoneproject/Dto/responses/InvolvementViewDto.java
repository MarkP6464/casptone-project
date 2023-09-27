package com.example.capstoneproject.Dto.responses;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
