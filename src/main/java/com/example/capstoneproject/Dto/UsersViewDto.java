package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.*;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Map;
import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UsersViewDto {
    private int id;

    private String name;

    private String avatar;

    private String phone;

    private String permissionWebsite;

    private String email;

    private String linkin;

    private Map<String, Object> snapshot;
    private String country;
    private Role role;

    private Set<SkillDto> skills;

    private Set<CertificationDto> certifications;

    private Set<EducationDto> educations;

    private Set<ExperienceDto> experiences ;
    
    private Set<InvolvementDto> involvements ;
    
    private Set<ProjectDto> projects ;
    
    private Set<SourceWorkDto> sourceWorks;
}
