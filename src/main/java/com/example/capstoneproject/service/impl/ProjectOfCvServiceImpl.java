package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.ProjectOfCvDto;
import com.example.capstoneproject.Dto.ProjectViewDto;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.CvStatus;
import com.example.capstoneproject.mapper.ProjectOfCvMapper;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.ProjectOfCvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectOfCvServiceImpl extends AbstractBaseService<ProjectOfCv, ProjectOfCvDto, Integer> implements ProjectOfCvService {
    @Autowired
    ProjectOfCvRepository projectOfCvRepository;

    @Autowired
    ProjectOfCvMapper projectOfCvMapper;

    @Autowired
    CvRepository cvRepository;

    @Autowired
    ProjectRepository projectRepository;

    public ProjectOfCvServiceImpl(ProjectOfCvRepository projectOfCvRepository, ProjectOfCvMapper projectOfCvMapper) {
        super(projectOfCvRepository, projectOfCvMapper, projectOfCvRepository::findById);
        this.projectOfCvRepository = projectOfCvRepository;
        this.projectOfCvMapper = projectOfCvMapper;
    }

    @Override
    public boolean createProjectOfCv(Integer cvId, Integer projectId) {
        Project project = projectRepository.findProjectById(projectId, CvStatus.ACTIVE);
        Cv cv = cvRepository.findCvById(cvId,CvStatus.ACTIVE);
        if (project != null && cv != null) {
            ProjectOfCv projectOfCv = new ProjectOfCv();
            projectOfCv.setProject(project);
            projectOfCv.setCv(cv);
            projectOfCvRepository.save(projectOfCv);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteProjectOfCv(Integer cvId, Integer projectId) {
        ProjectOfCv projectOfCv = projectOfCvRepository.findByProject_IdAndCv_Id(projectId, cvId);

        if (projectOfCv != null) {
            projectOfCvRepository.delete(projectOfCv);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<ProjectViewDto> getActiveProjectsByCvId(Integer cvId) {
        List<ProjectOfCv> projectOfCvs = projectOfCvRepository.findActiveProjectsByCvId(cvId, CvStatus.ACTIVE);
        List<ProjectViewDto> projectViewDtos = new ArrayList<>();

        for (ProjectOfCv projectOfCv : projectOfCvs) {
            Project project = projectOfCv.getProject();
            ProjectViewDto projectViewDto = new ProjectViewDto();
            projectViewDto.setId(project.getId());
            projectViewDto.setTitle(project.getTitle());
            projectViewDto.setOrganization(project.getOrganization());
            projectViewDto.setStartDate(project.getStartDate());
            projectViewDto.setEndDate(project.getEndDate());
            projectViewDto.setDescription(project.getDescription());
            projectViewDto.setProjectUrl(project.getProjectUrl());
            projectViewDtos.add(projectViewDto);
        }

        return projectViewDtos;
    }

    @Override
    public List<ProjectViewDto> getAllProject(int cvId) {
        List<Project> existingProjects = projectOfCvRepository.findProjectsByCvId(cvId);
        List<Project> activeProjects = projectRepository.findByStatus(CvStatus.ACTIVE);
        List<Project> unassignedProjects = activeProjects.stream()
                .filter(project -> !existingProjects.contains(project))
                .collect(Collectors.toList());

        return unassignedProjects.stream()
                .map(project -> {
                    ProjectViewDto projectViewDto = new ProjectViewDto();
                    projectViewDto.setId(project.getId());
                    projectViewDto.setTitle(project.getTitle());
                    projectViewDto.setOrganization(project.getOrganization());
                    projectViewDto.setStartDate(project.getStartDate());
                    projectViewDto.setEndDate(project.getEndDate());
                    projectViewDto.setDescription(project.getDescription());
                    projectViewDto.setProjectUrl(project.getProjectUrl());
                    return projectViewDto;
                })
                .collect(Collectors.toList());
    }
}
