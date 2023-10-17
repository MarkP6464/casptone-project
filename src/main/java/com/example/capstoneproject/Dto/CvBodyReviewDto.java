package com.example.capstoneproject.Dto;

import lombok.*;

import java.util.Set;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CvBodyReviewDto {
    private String name;

    private String address;

    private String phone;

    private String permissionWebsite;

    private String email;

    private String linkin;

    private  String summary;

    private Set<SkillDto> skills;

    private Set<CertificationDto> certifications;

    private Set<EducationDto> educations;

    private Set<ExperienceDto> experiences ;

    private Set<InvolvementDto> involvements;

    private Set<ProjectDto> projects ;

    private Set<SourceWorkDto> sourceWorks;
}
