package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.InvolvementViewDto;
import com.example.capstoneproject.Dto.ProjectDto;
import com.example.capstoneproject.Dto.ProjectViewDto;
import com.example.capstoneproject.Dto.SkillViewDto;
import com.example.capstoneproject.entity.Project;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService extends BaseService<ProjectDto, Integer> {
    ProjectDto update(Integer id, ProjectDto dto);
    boolean updateProject(Integer id, ProjectViewDto dto);
    List<ProjectViewDto> getAllProject(int cvId);
}
