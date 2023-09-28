package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.ProjectViewDto;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.exception.ResourceNotFoundException;
import com.example.capstoneproject.mapper.ProjectMapper;
import com.example.capstoneproject.repository.ProjectRepository;
import com.example.capstoneproject.service.CvService;
import com.example.capstoneproject.service.UsersService;
import com.example.capstoneproject.service.ProjectService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl extends AbstractBaseService<Project, ProjectDto, Integer> implements ProjectService {
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ProjectMapper projectMapper;

    @Autowired
    UsersService usersService;


    @Autowired
    CvService cvService;

    @Autowired
    ModelMapper modelMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        super(projectRepository, projectMapper, projectRepository::findById);
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    @Override
    public ProjectDto createProject(Integer id, ProjectDto dto) {
        Project project = projectMapper.mapDtoToEntity(dto);
        Users Users = usersService.getUsersById(id);
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

    @Override
    public ProjectDto getAndIsDisplay(int cvId, int id) throws JsonProcessingException {
        Project education = projectRepository.getById(id);
        if (Objects.nonNull(education)){
            Cv cv = cvService.getCvById(cvId);
            CvBodyDto cvBodyDto = cv.deserialize();
            Optional<ProjectDto> dto = cvBodyDto.getProjects().stream().filter(x -> x.getId()==id).findFirst();
            if (dto.isPresent()){
                modelMapper.map(education, dto.get());
                return dto.get();
            }else{
                throw new ResourceNotFoundException("Not found that id in cvBody");
            }
        }else{
            throw new ResourceNotFoundException("Not found that id in cvBody");
        }
    }

    @Override
    public ProjectDto getByIdInCvBody(int cvId, int id) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        Optional<ProjectDto> dto = cvBodyDto.getProjects().stream().filter(x -> x.getId()==id).findFirst();
        if (dto.isPresent()){
            return  dto.get();
        }else{
            throw new ResourceNotFoundException("Not found that id in cvBody");
        }
    }

    @Override
    public Set<ProjectDto> getAllARelationInCvBody(int cvId) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        return cvBodyDto.getProjects();
    }

    @Override
    public boolean updateInCvBody(int cvId, int id, ProjectDto dto) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        Optional<ProjectDto> relationDto = cvBodyDto.getProjects().stream().filter(x -> x.getId()==id).findFirst();
        if (relationDto.isPresent()) {
            Project education = projectRepository.getById(id);
            modelMapper.map(dto, education);
            projectRepository.save(education);
            ProjectDto educationDto = relationDto.get();
            educationDto.setIsDisplay(dto.getIsDisplay());
            cvService.updateCvBody(cvId, cvBodyDto);
            return true;
        } else {
            throw new IllegalArgumentException("education ID not found in cvBody");
        }
    }


    @Override
    public ProjectDto createOfUserInCvBody(int cvId, ProjectDto dto) throws JsonProcessingException {
        Project education = projectMapper.mapDtoToEntity(dto);
        Users Users = usersService.getUsersById(cvService.getCvById(cvId).getUser().getId());
        education.setUser(Users);
        education.setStatus(BasicStatus.ACTIVE);
        Project saved = projectRepository.save(education);
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(saved.getId());
        CvBodyDto cvBodyDto = cvService.getCvBody(cvId);
        cvBodyDto.getProjects().add(projectDto);
        projectDto.setIsDisplay(true);
        cvService.updateCvBody(cvId, cvBodyDto);
        return projectDto;
    }

    @Override
    public void deleteInCvBody(Integer cvId, Integer id) throws JsonProcessingException {

        Optional<Project> Optional = projectRepository.findById(id);
        if (Optional.isPresent()) {
            Project education = Optional.get();
            education.setStatus(BasicStatus.DELETED);
            projectRepository.save(education);
            CvBodyDto cvBodyDto = cvService.getCvBody(cvId);
            cvBodyDto.getEducations().removeIf(x -> x.getId() == id);
            cvService.updateCvBody(cvId, cvBodyDto);
        }
    }


}
