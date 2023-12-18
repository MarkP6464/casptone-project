package com.example.capstoneproject.Dto.request;

import com.example.capstoneproject.Dto.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class CvBodyRequest {
    private Boolean sharable = false;

    private Boolean searchable = false;

    private String resumeName;

    private String jobTitle;

    private String jobDescription;

    private String companyName;

//    private String fieldOrDomain;

//    private String experience;

    private String templateType = "classical";

    private String name;

    private String email;

    private String phone;

    private String linkin;

    private String personalWebsite;

    private String city;

    private CvStyleDto cvStyle = new CvStyleDto();

    private List<SkillDto> skills = new ArrayList<>();

    private List<CertificationDto> certifications = new ArrayList<>();

    private List<EducationDto> educations = new ArrayList<>();

    private List<ExperienceDto> experiences = new ArrayList<>();

    private List<InvolvementDto> involvements = new ArrayList<>();

    private List<ProjectDto> projects = new ArrayList<>();

    private List<SourceWorkDto> sourceWorks = new ArrayList<>();
}
