package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.ProjectDto;
import com.example.capstoneproject.Dto.responses.ProjectViewDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService extends BaseService<ProjectDto, Integer> {
    boolean updateProject(int UsersId, int projectId, ProjectDto dto);

    List<ProjectViewDto> getAllProject(int UsersId);

    ProjectDto createProject(Integer cvId, ProjectDto dto);

    void deleteProjectById(Integer UsersId, Integer projectId);

    ProjectViewDto getAndIsDisplay(int cvId, int id) throws JsonProcessingException;

    ProjectDto getByIdInCvBody(int cvId, int id) throws JsonProcessingException;

    List<ProjectViewDto> getAllARelationInCvBody(int cvId) throws JsonProcessingException;

    ProjectViewDto updateInCvBody(int cvId, int id, ProjectDto dto) throws JsonProcessingException;

    ProjectViewDto createOfUserInCvBody(int cvId, ProjectDto dto) throws JsonProcessingException;

    void deleteInCvBody(Integer cvId, Integer id) throws JsonProcessingException;
}
