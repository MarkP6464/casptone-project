package com.example.capstoneproject.Dto.responses;

import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectViewDto {
    private int id;

    private Boolean isDisplay;

    private String Title;

    private String Organization;

    private Date StartDate;

    private Date EndDate;

    private String ProjectUrl;

    private String Description;

}
