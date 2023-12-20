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

    private Boolean sharable;

    private Boolean searchable;
    private String email;
    private String personalWebsite;
    private String phone;
    private String name;
    private String resumeName;
    private String companyName;
    private String jobTitle;
    private String fieldOrDomain;
    private String jobDescription;
    private String experience;
    private String linkin;
    private String city;
    private Long id;
    private String content;
    private String summary;
    private TheOrder theOrder;
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
