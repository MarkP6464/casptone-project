package com.example.capstoneproject.Dto;

import com.example.capstoneproject.enums.BasicStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor

@Getter
@Setter
public class CvAddNewDto {
    private String email;
    private String personalWebsite;
    private String phone;
    private String name;
    private String resumeName;

    private String fieldOrDomain;

    private String experience;
    private Long id;
    private String content;
    private String summary;
    private BasicStatus status;
    private String templateType;
    private CvStyleDto cvStyle;
    private List<SkillDto> skills;
    private List<CertificationDto> certifications;
    private List<EducationDto> educations;
    private List<ExperienceDto> experiences;
    private List<InvolvementDto> involvements;
    private List<ProjectDto> projects;

    private List<SourceWorkDto> sourceWorks;

}
