package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.Project;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService extends BaseService<ProjectDto, Integer> {
    boolean updateProject(int customerId, int projectId, ProjectDto dto);
    List<ProjectViewDto> getAllProject(int customerId);
    ProjectDto createProject(Integer id, ProjectDto dto);
    void deleteProjectById(Integer customerId,Integer projectId);
}
