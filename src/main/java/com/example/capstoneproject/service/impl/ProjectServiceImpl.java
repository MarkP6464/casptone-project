package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.mapper.ProjectMapper;
import com.example.capstoneproject.repository.ProjectRepository;
import com.example.capstoneproject.service.UsersService;
import com.example.capstoneproject.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl extends AbstractBaseService<Project, ProjectDto, Integer> implements ProjectService {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    UsersService UsersService;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        super(projectRepository, projectMapper, projectRepository::findById);
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    @Override
    public ProjectDto createProject(Integer id, ProjectDto dto) {
        Project project = projectMapper.mapDtoToEntity(dto);
        Users Users = UsersService.getUsersById(id);
        project.setUser(Users);
        project.setStatus(BasicStatus.ACTIVE);
        Project saved = projectRepository.save(project);
        return projectMapper.mapEntityToDto(saved);
    }

    @Override
    public boolean updateProject(int UsersId, int projectId, ProjectDto dto) {
        Optional<Project> existingProjectOptional = projectRepository.findById(projectId);
        if (existingProjectOptional.isPresent()) {
            Project existingProject = existingProjectOptional.get();
            if (existingProject.getUser().getId() != UsersId) {
                throw new IllegalArgumentException("Project does not belong to Users with id " + UsersId);
            }
            if (dto.getTitle() != null && !existingProject.getTitle().equals(dto.getTitle())) {
                existingProject.setTitle(dto.getTitle());
            } else {
                existingProject.setTitle(existingProject.getTitle());
            }
            if (dto.getOrganization() != null && !existingProject.getOrganization().equals(dto.getOrganization())) {
                existingProject.setOrganization(dto.getOrganization());
            } else {
                existingProject.setOrganization(existingProject.getOrganization());
            }
            if (dto.getStartDate() != null && !existingProject.getStartDate().equals(dto.getStartDate())) {
                existingProject.setStartDate(dto.getStartDate());
            } else {
                existingProject.setStartDate(existingProject.getStartDate());
            }
            if (dto.getEndDate() != null && !existingProject.getEndDate().equals(dto.getEndDate())) {
                existingProject.setEndDate(dto.getEndDate());
            } else {
                existingProject.setEndDate(existingProject.getEndDate());
            }
            if (dto.getProjectUrl() != null && !existingProject.getProjectUrl().equals(dto.getProjectUrl())) {
                existingProject.setProjectUrl(dto.getProjectUrl());
            } else {
                existingProject.setProjectUrl(existingProject.getProjectUrl());
            }
            if (dto.getDescription() != null && !existingProject.getDescription().equals(dto.getDescription())) {
                existingProject.setDescription(dto.getDescription());
            } else {
                existingProject.setDescription(existingProject.getDescription());
            }

            existingProject.setStatus(BasicStatus.ACTIVE);
            Project updated = projectRepository.save(existingProject);
            return true;
        } else {
            throw new IllegalArgumentException("Project ID not found");
        }
    }

    @Override
    public List<ProjectViewDto> getAllProject(int UsersId) {
        List<Project> projects = projectRepository.findProjectsByStatus(UsersId, BasicStatus.ACTIVE);
        return projects.stream()
                .filter(project -> project.getStatus() == BasicStatus.ACTIVE)
                .map(project -> {
                    ProjectViewDto projectViewDto = new ProjectViewDto();
                    projectViewDto.setId(project.getId());
                    projectViewDto.setTitle(project.getTitle());
                    projectViewDto.setOrganization(project.getOrganization());
                    projectViewDto.setStartDate(project.getStartDate());
                    projectViewDto.setEndDate(project.getEndDate());
                    projectViewDto.setProjectUrl(project.getProjectUrl());
                    projectViewDto.setDescription(project.getDescription());
                    return projectViewDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProjectById(Integer UsersId,Integer projectId) {
        boolean isProjectBelongsToCv = projectRepository.existsByIdAndUser_Id(projectId, UsersId);

        if (isProjectBelongsToCv) {
            Optional<Project> Optional = projectRepository.findById(projectId);
            if (Optional.isPresent()) {
                Project project = Optional.get();
                project.setStatus(BasicStatus.DELETED);
                projectRepository.save(project);
            }
        } else {
            throw new IllegalArgumentException("Project with ID " + projectId + " does not belong to Users with ID " + UsersId);
        }
    }

}
