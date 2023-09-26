package com.example.capstoneproject.Dto;

import com.example.capstoneproject.enums.BasicStatus;
import lombok.*;

import java.util.Map;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CvBodyDto {
    private int cvId;

    private String Content;

    private String Summary;

    private BasicStatus Status;

    private TemplateViewDto template;
    private Set<SkillDto> skills;

    private Set<CertificationDto> certifications;

    private Set<EducationDto> educations;

    private Set<ExperienceDto> experiences ;

    private Set<InvolvementDto> involvements;

    private Set<ProjectDto> projects ;

    private Set<SourceWorkDto> sourceWorks;
}
