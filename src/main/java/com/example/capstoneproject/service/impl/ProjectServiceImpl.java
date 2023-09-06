package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.InvolvementDto;
import com.example.capstoneproject.Dto.InvolvementViewDto;
import com.example.capstoneproject.Dto.ProjectDto;
import com.example.capstoneproject.Dto.ProjectViewDto;
import com.example.capstoneproject.entity.Involvement;
import com.example.capstoneproject.entity.Project;
import com.example.capstoneproject.enums.CvStatus;
import com.example.capstoneproject.mapper.InvolvementMapper;
import com.example.capstoneproject.mapper.ProjectMapper;
import com.example.capstoneproject.repository.InvolvementRepository;
import com.example.capstoneproject.repository.ProjectRepository;
import com.example.capstoneproject.service.InvolvementService;
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

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        super(projectRepository, projectMapper, projectRepository::findById);
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    @Override
    public boolean updateProject(Integer id, ProjectViewDto dto) {
        Optional<Project> existingProjectOptional = projectRepository.findById(id);
        if (existingProjectOptional.isPresent()) {
            Project existingProject = existingProjectOptional.get();
            if (dto.getTitle() != null && !existingProject.getTitle().equals(dto.getTitle())) {
                existingProject.setTitle(dto.getTitle());
            } else {
                throw new IllegalArgumentException("New Title is the same as the existing project");
            }
            if (dto.getOrganization() != null && !existingProject.getOrganization().equals(dto.getOrganization())) {
                existingProject.setOrganization(dto.getOrganization());
            } else {
                throw new IllegalArgumentException("New Organization is the same as the existing project");
            }
            if (dto.getStartDate() != null && !existingProject.getStartDate().equals(dto.getStartDate())) {
                existingProject.setStartDate(dto.getStartDate());
            } else {
                throw new IllegalArgumentException("New Start Date is the same as the existing project");
            }
            if (dto.getEndDate() != null && !existingProject.getEndDate().equals(dto.getEndDate())) {
                existingProject.setEndDate(dto.getEndDate());
            } else {
                throw new IllegalArgumentException("New End Date is the same as the existing project");
            }
            if (dto.getUrl() != null && !existingProject.getUrl().equals(dto.getUrl())) {
                existingProject.setUrl(dto.getUrl());
            } else {
                throw new IllegalArgumentException("New Url is the same as the existing project");
            }
            if (dto.getDescription() != null && !existingProject.getDescription().equals(dto.getDescription())) {
                existingProject.setDescription(dto.getDescription());
            } else {
                throw new IllegalArgumentException("New Description is the same as the existing project");
            }

            existingProject.setStatus(CvStatus.ACTIVE);
            Project updated = projectRepository.save(existingProject);
            return true;
        } else {
            throw new IllegalArgumentException("Project ID not found");
        }
    }

    @Override
    public List<ProjectViewDto> getAllProject(int cvId) {
        List<Project> projects = projectRepository.findProjectsByStatus(cvId,CvStatus.ACTIVE);
        return projects.stream()
                .filter(project -> project.getStatus() == CvStatus.ACTIVE)
                .map(project -> {
                    ProjectViewDto projectViewDto = new ProjectViewDto();
                    projectViewDto.setId(project.getId());
                    projectViewDto.setTitle(project.getTitle());
                    projectViewDto.setOrganization(project.getOrganization());
                    projectViewDto.setStartDate(project.getStartDate());
                    projectViewDto.setEndDate(project.getEndDate());
                    projectViewDto.setUrl(project.getUrl());
                    projectViewDto.setDescription(project.getDescription());
                    projectViewDto.setStatus(project.getStatus());
                    return projectViewDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Integer id) {
        Optional<Project> Optional = projectRepository.findById(id);
        if (Optional.isPresent()) {
            Project project = Optional.get();
            project.setStatus(CvStatus.DELETED);
            projectRepository.save(project);
        }
    }

}
