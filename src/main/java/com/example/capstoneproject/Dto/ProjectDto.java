package com.example.capstoneproject.Dto;

import com.example.capstoneproject.enums.BasicStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor

@Getter
@Setter
public class ProjectDto {
    private Integer id;

    private Integer theOrder;

    private Boolean isDisplay = true;
    private String Title;

    private String Organization;

    private String duration;

    private String ProjectUrl;

    private String Description;
    private BasicStatus Status;
}
