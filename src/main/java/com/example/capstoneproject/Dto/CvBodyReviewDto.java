package com.example.capstoneproject.Dto;

import lombok.*;

import java.util.HashMap;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CvBodyReviewDto {
    private String templateType = "classical";

    private CvStyleDto cvStyle = new CvStyleDto();

    private HashMap<String, Long> theOrder;

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

    private List<CustomizeSectionDto> customSections;

    private List<SourceWorkDto> sourceWorks;
}
