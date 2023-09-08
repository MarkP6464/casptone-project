package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.Project;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService extends BaseService<ProjectDto, Integer> {
    ProjectDto update(Integer id, ProjectDto dto);
    boolean updateProject(int cvId, int projectId, ProjectDto dto);
    List<ProjectViewDto> getAllProject(int cvId);
    ProjectDto createProject(Integer id, ProjectDto dto);
    void deleteProjectById(Integer cvId,Integer projectId);
}
