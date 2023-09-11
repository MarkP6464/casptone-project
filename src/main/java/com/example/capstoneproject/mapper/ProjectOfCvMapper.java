package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.ProjectOfCvDto;
import com.example.capstoneproject.entity.ProjectOfCv;
import org.springframework.stereotype.Component;

@Component
public class ProjectOfCvMapper extends AbstractMapper<ProjectOfCv, ProjectOfCvDto> {
    public ProjectOfCvMapper() {
        super(ProjectOfCv.class, ProjectOfCvDto.class);
    }
}
