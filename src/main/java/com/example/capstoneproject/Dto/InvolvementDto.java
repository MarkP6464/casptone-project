package com.example.capstoneproject.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor

@Getter
@Setter
public class InvolvementDto {
    private Integer id;
    private Integer theOrder;

    private String OrganizationRole;

    private Boolean isDisplay = true;
    private String OrganizationName;

    private String duration;

    private String College;

    private String Description;
}
