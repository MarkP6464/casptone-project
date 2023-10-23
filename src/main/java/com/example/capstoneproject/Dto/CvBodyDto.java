package com.example.capstoneproject.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor

@Getter
@Setter
@ToString
public class CvBodyDto {
    private String resumeName;

    private String fieldOrDomain;

    private String experience;
    private String templateType = "classical";
    private CvStyleDto cvStyle = new CvStyleDto();

    private List<SkillDto> skills = new ArrayList<>();

    private List<CertificationDto> certifications = new ArrayList<>();

    private List<EducationDto> educations = new ArrayList<>();

    private List<ExperienceDto> experiences = new ArrayList<>();

    private List<InvolvementDto> involvements = new ArrayList<>();

    private List<ProjectDto> projects = new ArrayList<>();

    private List<SourceWorkDto> sourceWorks = new ArrayList<>();
}
