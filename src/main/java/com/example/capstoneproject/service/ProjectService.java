package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.ProjectDto;
import org.springframework.stereotype.Service;

@Service
public interface ProjectService extends BaseService<ProjectDto, Integer> {
    ProjectDto update(Integer id, ProjectDto dto);
}
