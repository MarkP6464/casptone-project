package com.example.capstoneproject.Dto;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectDto {

    private String Title;

    private String Organization;

    private Date StartDate;

    private Date EndDate;

    private String ProjectUrl;

    private String Description;
}
