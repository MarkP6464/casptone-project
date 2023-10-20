package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.Dto.BulletPointDto;
import com.example.capstoneproject.Dto.ResultDto;
import lombok.*;

import java.util.Date;
import java.util.List;


@NoArgsConstructor
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

    private List<BulletPointDto> bulletPointDtos;
}
