package com.example.capstoneproject.Dto;

import lombok.*;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;


@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class UsersViewDto implements Serializable {
    private Integer id;

    private String name;

    private String avatar;

    private String phone;

    private String personalWebsite;

    @Pattern(regexp = "^\\S+@\\S+\\.\\S+$", message = "Please check your email format!!")    private String email;
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
