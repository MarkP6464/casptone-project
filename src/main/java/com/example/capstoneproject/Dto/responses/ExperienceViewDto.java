package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.Dto.BulletPointDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@Getter
@Setter
public class ExperienceViewDto {
    private int id;

    private Boolean isDisplay;

    private String Role;

    private String CompanyName;

    private String duration;

    private String Location;

    private String Description;

    private List<BulletPointDto> bulletPointDtos;

    private Integer theOrder;
}
