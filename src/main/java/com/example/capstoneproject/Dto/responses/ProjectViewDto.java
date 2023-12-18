package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.Dto.BulletPointDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor

@Getter
@Setter
public class ProjectViewDto {
    private int id;

    private Boolean isDisplay;

    private String Title;

    private String Organization;

    private String duration;

    private String ProjectUrl;

    private String Description;

    private List<BulletPointDto> bulletPointDtos;

    private Integer theOrder;
}
