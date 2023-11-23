package com.example.capstoneproject.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@Getter
@Setter
public class UsersViewDto {
    private Integer id;

    private String name;

    private String avatar;

    private String phone;

    private String personalWebsite;

    private String email;

    private String linkin;

    private String country;
    private String status;

    private List<SkillDto> skills;

    private List<CertificationDto> certifications;

    private List<EducationDto> educations;

    private List<ExperienceDto> experiences;

    private List<InvolvementDto> involvements;

    private List<ProjectDto> projects;
    private List<CvDto> cvs;
    private List<SourceWorkDto> sourceWorks;
}
