package com.example.capstoneproject.Dto;

import lombok.*;

import java.util.List;
import java.util.Set;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CvBodyReviewDto {
    private String templateType = "classical";

    private CvStyleDto cvStyle = new CvStyleDto();

    private TheOrder theOrder;

    private String name;

    private String address;

    private String phone;

    private String personalWebsite;

    private String email;

    private String linkin;

    private String summary;

    private List<SkillDto> skills;

    private List<CertificationDto> certifications;

    private List<EducationDto> educations;

    private List<ExperienceDto> experiences;

    private List<InvolvementDto> involvements;

    private List<ProjectDto> projects;

    private List<SourceWorkDto> sourceWorks;
}
