package com.example.capstoneproject.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;


@NoArgsConstructor

@Getter
@Setter
@ToString
public class CvBodyDto {
    private String templateType = "classical";
    private CvStyleDto cvStyle = new CvStyleDto();

    private Set<SkillDto> skills = new HashSet<>();

    private Set<CertificationDto> certifications = new HashSet<>();

    private Set<EducationDto> educations = new HashSet<>();

    private Set<ExperienceDto> experiences = new HashSet<>();

    private Set<InvolvementDto> involvements = new HashSet<>();

    private Set<ProjectDto> projects = new HashSet<>();

    private Set<SourceWorkDto> sourceWorks = new HashSet<>();
}
