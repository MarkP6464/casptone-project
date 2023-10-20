package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.Dto.BulletPointDto;
import lombok.*;

import java.util.Date;
import java.util.List;


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

    private List<BulletPointDto> bulletPointDtos;
}
