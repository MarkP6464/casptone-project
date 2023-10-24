package com.example.capstoneproject.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor

@Getter
@Setter
public class ProjectDto {
    private Integer id;

    private Boolean isDisplay;
    private String Title;

    private String Organization;

    private String duration;

    private String ProjectUrl;

    private String Description;
}
