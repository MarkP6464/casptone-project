package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.InvolvementDto;
import com.example.capstoneproject.Dto.ProjectDto;
import com.example.capstoneproject.entity.Involvement;
import com.example.capstoneproject.entity.Project;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper extends AbstractMapper<Project, ProjectDto> {
    public ProjectMapper() {
        super(Project.class, ProjectDto.class);
    }
}
