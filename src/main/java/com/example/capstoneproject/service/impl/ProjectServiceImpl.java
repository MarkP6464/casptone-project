package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.ExperienceViewDto;
import com.example.capstoneproject.Dto.responses.ProjectViewDto;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.SectionEvaluate;
import com.example.capstoneproject.exception.ResourceNotFoundException;
import com.example.capstoneproject.mapper.ProjectMapper;
import com.example.capstoneproject.mapper.SectionMapper;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    SectionService sectionService;

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    SectionMapper sectionMapper;

    @Autowired
    SectionLogService sectionLogService;

    @Autowired
    SectionLogRepository sectionLogRepository;

    @Autowired
    EvaluateService evaluateService;

    @Autowired
    EvaluateRepository evaluateRepository;

    @Autowired
    CvService cvService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CvRepository cvRepository;

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
    public void deleteProjectById(Integer UsersId, Integer projectId) {
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
    public ProjectViewDto getAndIsDisplay(int cvId, int id) throws JsonProcessingException {
        Project project = projectRepository.getById(id);
        if (Objects.nonNull(project)) {
            Cv cv = cvService.getCvById(cvId);
            CvBodyDto cvBodyDto = cv.deserialize();
            Optional<ProjectDto> dto = cvBodyDto.getProjects().stream().filter(x -> x.getId() == id).findFirst();
            List<BulletPointDto> bulletPointDtos = sectionRepository.findBulletPointDtoByTypeIdAndTypeName(id, SectionEvaluate.project);
            if (dto.isPresent()) {
                ProjectDto projectDto = dto.get();
                ProjectViewDto projectViewDto = new ProjectViewDto();
                projectViewDto.setId(project.getId());
                projectViewDto.setIsDisplay(projectDto.getIsDisplay());
                projectViewDto.setTitle(project.getTitle());
                projectViewDto.setOrganization(project.getOrganization());
                projectViewDto.setStartDate(project.getStartDate());
                projectViewDto.setEndDate(project.getEndDate());
                projectViewDto.setProjectUrl(project.getProjectUrl());
                projectViewDto.setDescription(project.getDescription());
                projectViewDto.setBulletPointDtos(bulletPointDtos);
                return projectViewDto;
            } else {
                throw new ResourceNotFoundException("Not found that id in cvBody");
            }
        } else {
            throw new ResourceNotFoundException("Not found that id in cvBody");
        }
    }

    @Override
    public ProjectDto getByIdInCvBody(int cvId, int id) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        Optional<ProjectDto> dto = cvBodyDto.getProjects().stream().filter(x -> x.getId() == id).findFirst();
        if (dto.isPresent()) {
            return dto.get();
        } else {
            throw new ResourceNotFoundException("Not found that id in cvBody");
        }
    }

    @Override
    public List<ProjectViewDto> getAllARelationInCvBody(int cvId) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        List<ProjectViewDto> set = new ArrayList<>();
        cvBodyDto.getProjects().stream().forEach(
                e -> {
                    try {
                        set.add(getAndIsDisplay(cvId, e.getId()));
                    } catch (JsonProcessingException ex) {
                        throw new RuntimeException(ex);
                    }
                }
        );
        return set;
    }

    @Override
    public ProjectViewDto updateInCvBody(int cvId, int id, ProjectDto dto) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        Optional<ProjectDto> relationDto = cvBodyDto.getProjects().stream().filter(x -> x.getId() == id).findFirst();
        if (relationDto.isPresent()) {
            Project project = projectRepository.getById(id);
            modelMapper.map(dto, project);
            Project saved = projectRepository.save(project);
            ProjectDto educationDto = relationDto.get();
            educationDto.setIsDisplay(dto.getIsDisplay());
            cvService.updateCvBody(cvId, cvBodyDto);

            //Delete section_log in db
            Section section = sectionRepository.findByTypeNameAndTypeId(SectionEvaluate.project, project.getId());
            sectionLogRepository.deleteBySection_Id(section.getId());
            //Get process evaluate
            List<BulletPointDto> evaluateResult = evaluateService.checkSentences(dto.getDescription());
            ProjectViewDto projectViewDto = new ProjectViewDto();
            projectViewDto.setId(saved.getId());
            projectViewDto.setIsDisplay(dto.getIsDisplay());
            projectViewDto.setTitle(saved.getTitle());
            projectViewDto.setOrganization(saved.getOrganization());
            projectViewDto.setStartDate(saved.getStartDate());
            projectViewDto.setEndDate(saved.getEndDate());
            projectViewDto.setProjectUrl(saved.getProjectUrl());
            projectViewDto.setDescription(saved.getDescription());
            projectViewDto.setBulletPointDtos(evaluateResult);

            //Save evaluateLog into db
            List<Evaluate> evaluates = evaluateRepository.findAll();
            int evaluateId = 1;
            for (int i = 0; i < evaluates.size(); i++) {
                Evaluate evaluate = evaluates.get(i);
                BulletPointDto bulletPointDto = evaluateResult.get(i);
                SectionLogDto sectionLogDto1 = new SectionLogDto();
                sectionLogDto1.setSection(sectionMapper.mapDtoToEntity(sectionMapper.mapEntityToDto(section)));
                sectionLogDto1.setEvaluate(evaluate);
                sectionLogDto1.setBullet(bulletPointDto.getResult());
                sectionLogDto1.setStatus(bulletPointDto.getStatus());
                sectionLogService.create(sectionLogDto1);
                evaluateId++;
                if (evaluateId == 7) {
                    break;
                }
            }
            return projectViewDto;
        } else {
            throw new IllegalArgumentException("education ID not found in cvBody");
        }
    }


    @Override
    public ProjectViewDto createOfUserInCvBody(int cvId, ProjectDto dto) throws JsonProcessingException {
        Project education = projectMapper.mapDtoToEntity(dto);
        Users user = usersService.getUsersById(cvService.getCvById(cvId).getUser().getId());
        education.setUser(user);
        education.setStatus(BasicStatus.ACTIVE);
        Project saved = projectRepository.save(education);
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(saved.getId());
        List<Cv> list = cvRepository.findAllByUsersIdAndStatus(user.getId(), BasicStatus.ACTIVE);
        list.stream().forEach(x -> {
            if (x.getId().equals(cvId)) {
                projectDto.setIsDisplay(true);
            } else {
                projectDto.setIsDisplay(false);
            }
            try {
                CvBodyDto cvBodyDto = x.deserialize();
                cvBodyDto.getProjects().add(projectDto);
                cvService.updateCvBody(x.getId(), cvBodyDto);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

        //Save evaluate db
        SectionDto sectionDto = new SectionDto();
        List<Project> projects = projectRepository.findExperiencesByStatusOrderedByStartDateDesc(user.getId(), BasicStatus.ACTIVE);
        if (!projects.isEmpty()) {
            sectionDto.setTypeId(projects.get(0).getId());
        }
        sectionDto.setTitle(saved.getTitle());
        sectionDto.setTypeName(SectionEvaluate.project);
        SectionDto section = sectionService.create(sectionDto);

        //Get process evaluate
        List<BulletPointDto> evaluateResult = evaluateService.checkSentences(dto.getDescription());
        ProjectViewDto projectViewDto = new ProjectViewDto();
        projectViewDto.setId(saved.getId());
        projectViewDto.setIsDisplay(true);
        projectViewDto.setTitle(saved.getTitle());
        projectViewDto.setOrganization(saved.getOrganization());
        projectViewDto.setStartDate(saved.getStartDate());
        projectViewDto.setEndDate(saved.getEndDate());
        projectViewDto.setProjectUrl(saved.getProjectUrl());
        projectViewDto.setDescription(saved.getDescription());
        projectViewDto.setBulletPointDtos(evaluateResult);

        //Save evaluateLog into db
        List<Evaluate> evaluates = evaluateRepository.findAll();
        int evaluateId = 1;
        for (int i = 0; i < evaluates.size(); i++) {
            Evaluate evaluate = evaluates.get(i);
            BulletPointDto bulletPointDto = evaluateResult.get(i);
            SectionLogDto sectionLogDto1 = new SectionLogDto();
            sectionLogDto1.setSection(sectionMapper.mapDtoToEntity(section));
            sectionLogDto1.setEvaluate(evaluate);
            sectionLogDto1.setBullet(bulletPointDto.getResult());
            sectionLogDto1.setStatus(bulletPointDto.getStatus());
            sectionLogService.create(sectionLogDto1);
            evaluateId++;
            if (evaluateId == 7) {
                break;
            }
        }
        return projectViewDto;
    }

    @Override
    public void deleteInCvBody(Integer cvId, Integer id) throws JsonProcessingException {

        Optional<Project> Optional = projectRepository.findById(id);
        if (Optional.isPresent()) {
            Project education = Optional.get();
            education.setStatus(BasicStatus.DELETED);
            projectRepository.delete(education);
            List<Cv> list = cvRepository.findAllByUsersIdAndStatus(education.getUser().getId(), BasicStatus.ACTIVE);
            list.stream().forEach(x -> {
                try {
                    CvBodyDto cvBodyDto = cvService.getCvBody(cvId);
                    cvBodyDto.getProjects().removeIf(e -> e.getId() == id);
                    cvService.updateCvBody(x.getId(), cvBodyDto);

                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }


}
