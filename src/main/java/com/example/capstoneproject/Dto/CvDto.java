package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.CvStatus;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CvDto {
    private int id;

    private String Content;

    private String Summary;

    private CustomerViewDto customer;

    private TemplateViewDto template;
    private List<EducationViewDto> educations;
    private List<ExperienceViewDto> experiences;
    private List<InvolvementViewDto> involvements;
    private List<ProjectViewDto> projects;
    private List<SourceWorkViewDto> sourceWorks;

}
