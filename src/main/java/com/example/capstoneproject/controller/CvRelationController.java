package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/cv")
public class CvRelationController {
    @Autowired
    EducationService educationService;

    @Autowired
    SkillService skillService;
    @Autowired
    CvService cvService;
    @Autowired
    ExperienceService experienceService;
    @Autowired
    InvolvementService involvementService;
    @Autowired
    ProjectService projectService;

    @Autowired
    CertificationService certificationService;

    @Autowired
    ObjectMapper objectMapper;

    @PutMapping(value = "/{cvId}/{theRelation}/update-all", consumes = "application/json")
    @PreAuthorize("hasAnyAuthority('create:candidate','create:candidate')")
    public ResponseEntity<?> post(@PathVariable("cvId") int cvId, @PathVariable("theRelation") String theRelation, @RequestBody List<Object> list) throws Exception {

        switch (theRelation) {
            case "educations":
                List<EducationDto> dtoList = list.stream().map(obj -> {
                    EducationDto educationDto = objectMapper.convertValue(obj, EducationDto.class);
                    return educationDto;
                }).collect(Collectors.toList());
                dtoList.stream().forEach(x -> {
                    try {
                        educationService.updateInCvBody(cvId, x.getId(), x);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
                return ResponseEntity.ok("Update the orders successfully");
            case "certifications":
                List<CertificationDto> certList = list.stream().map(obj -> {
                    CertificationDto educationDto = objectMapper.convertValue(obj, CertificationDto.class);
                    return educationDto;
                }).collect(Collectors.toList());
                certList.stream().forEach(x -> {
                    try {
                        certificationService.updateInCvBody(cvId, x.getId(), x);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
                return ResponseEntity.ok("Update the orders successfully");
            case "skills":
                List<SkillDto> skillDtoList = list.stream().map(obj -> {
                    SkillDto educationDto = objectMapper.convertValue(obj, SkillDto.class);
                    return educationDto;
                }).collect(Collectors.toList());
                skillDtoList.stream().forEach(x -> {
                    try {
                        skillService.updateInCvBody(cvId, x.getId(), x);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
                return ResponseEntity.ok("Update the orders successfully");
            case "involvements":
                List<InvolvementDto> involList = list.stream().map(obj -> {
                    InvolvementDto educationDto = objectMapper.convertValue(obj, InvolvementDto.class);
                    return educationDto;
                }).collect(Collectors.toList());
                involList.stream().forEach(x -> {
                    try {
                        involvementService.updateInCvBody(cvId, x.getId(), x);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
                return ResponseEntity.ok("Update the orders successfully");
            case "projects":
                List<ProjectDto> projectDtos = list.stream().map(obj -> {
                    ProjectDto educationDto = objectMapper.convertValue(obj, ProjectDto.class);
                    return educationDto;
                }).collect(Collectors.toList());
                projectDtos.stream().forEach(x -> {
                    try {
                        projectService.updateInCvBody(cvId, x.getId(), x);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
                return ResponseEntity.ok("Update the orders successfully");
            case "experiences":
                List<ExperienceDto> experienceDtos = list.stream().map(obj -> {
                    ExperienceDto educationDto = objectMapper.convertValue(obj, ExperienceDto.class);
                    return educationDto;
                }).collect(Collectors.toList());
                experienceDtos.stream().forEach(x -> {
                    try {
                        experienceService.updateInCvBody(cvId, x.getId(), x);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
                return ResponseEntity.ok("Update the orders successfully");
            default:
                throw new Exception("Invalid request!!");
        }
    }

    @GetMapping("/{cvId}/{theRelation}")
    @PreAuthorize("hasAnyAuthority('read:candidate', 'read:expert')")
    public List<?> getAllARelation(@PathVariable("cvId") int cvId, @PathVariable("theRelation") String theRelation) throws Exception {

        switch (theRelation) {
            case "educations":
                return educationService.getAllARelationInCvBody(cvId);
            case "skills":
                return skillService.getAllARelationInCvBody(cvId);
            case "experiences":
                return experienceService.getAllARelationInCvBody(cvId);
            case "involvements":
                return involvementService.getAllARelationInCvBody(cvId);
            case "projects":
                return projectService.getAllARelationInCvBody(cvId);
            case "certifications":
                return certificationService.getAllARelationInCvBody(cvId);
            default:
                throw new Exception("Invalid request!!");
        }
    }

    @GetMapping("/{cvId}/{theRelation}/{id}")
    @PreAuthorize("hasAnyAuthority('read:candidate', 'read:expert')")
    public ResponseEntity<?> getARelation(@PathVariable("cvId") int cvId, @PathVariable("theRelation") String theRelation, @PathVariable("id") int id) throws Exception {

        switch (theRelation) {
            case "educations":
                return ResponseEntity.ok(educationService.getAndIsDisplay(cvId, id));
            case "skills":
                return ResponseEntity.ok(skillService.getAndIsDisplay(cvId, id));
            case "experiences":
                return ResponseEntity.ok(experienceService.getAndIsDisplay(cvId, id));
            case "involvements":
                return ResponseEntity.ok(involvementService.getAndIsDisplay(cvId, id));
            case "projects":
                return ResponseEntity.ok(projectService.getAndIsDisplay(cvId, id));
            case "certifications":
                return ResponseEntity.ok(certificationService.getAndIsDisplay(cvId, id));
            default:
                throw new Exception("Invalid request!!");
        }
    }

    @PostMapping(value = "/{cvId}/{theRelation}", consumes = "application/json")
    @PreAuthorize("hasAnyAuthority('create:candidate','create:candidate')")
    public ResponseEntity<?> post(@PathVariable("cvId") int cvId, @PathVariable("theRelation") String theRelation,@Valid @RequestBody Object obj) throws Exception {

        switch (theRelation) {
            case "educations":
                EducationDto educationDto = objectMapper.convertValue(obj, EducationDto.class);
                return ResponseEntity.ok(educationService.createOfUserInCvBody(cvId, educationDto));
            case "skills":
                SkillDto skillDto = objectMapper.convertValue(obj, SkillDto.class);
                return ResponseEntity.ok(skillService.createOfUserInCvBody(cvId, skillDto));
            case "experiences":
                ExperienceDto experienceDto = objectMapper.convertValue(obj, ExperienceDto.class);
                return ResponseEntity.ok(experienceService.createOfUserInCvBody(cvId, experienceDto));
            case "involvements":
                InvolvementDto involvementDto = objectMapper.convertValue(obj, InvolvementDto.class);
                return ResponseEntity.ok(involvementService.createOfUserInCvBody(cvId, involvementDto));
            case "projects":
                ProjectDto projectDto = objectMapper.convertValue(obj, ProjectDto.class);
                return ResponseEntity.ok(projectService.createOfUserInCvBody(cvId, projectDto));
            case "certifications":
                CertificationDto certificationDto = objectMapper.convertValue(obj, CertificationDto.class);
                return ResponseEntity.ok(certificationService.createOfUserInCvBody(cvId, certificationDto));
            default:
                throw new Exception("Invalid request!!");
        }
    }

    @PutMapping("/{cvId}/{theRelation}/{id}")
    @PreAuthorize("hasAnyAuthority('update:candidate','update:expert')")
    public ResponseEntity<?> update(@PathVariable("cvId") int cvId, @PathVariable("id") int id, @PathVariable("theRelation") String theRelation, @RequestBody Object obj) throws Exception {
        switch (theRelation) {
            case "educations":
                EducationDto educationDto = objectMapper.convertValue(obj, EducationDto.class);
                return ResponseEntity.ok(educationService.updateInCvBody(cvId, id, educationDto));
            case "skills":
                SkillDto skillDto = objectMapper.convertValue(obj, SkillDto.class);
                return ResponseEntity.ok(skillService.updateInCvBody(cvId, id, skillDto));
            case "experiences":
                ExperienceDto experienceDto = objectMapper.convertValue(obj, ExperienceDto.class);
                return ResponseEntity.ok(experienceService.updateInCvBody(cvId, id, experienceDto));
            case "involvements":
                InvolvementDto involvementDto = objectMapper.convertValue(obj, InvolvementDto.class);
                return ResponseEntity.ok(involvementService.updateInCvBody(cvId, id, involvementDto));
            case "projects":
                ProjectDto projectDto = objectMapper.convertValue(obj, ProjectDto.class);
                return ResponseEntity.ok(projectService.updateInCvBody(cvId, id, projectDto));
            case "certifications":
                CertificationDto certificationDto = objectMapper.convertValue(obj, CertificationDto.class);
                return ResponseEntity.ok(certificationService.updateInCvBody(cvId, id, certificationDto));
            default:
                throw new Exception("Invalid request!!");
        }
    }

    @DeleteMapping("/{cvId}/{theRelation}/{id}")
    @PreAuthorize("hasAnyAuthority('delete:candidate','delete:expert')")
    public String deleteARelation(@PathVariable("cvId") int cvId, @PathVariable("theRelation") String theRelation, @PathVariable("id") int id) throws Exception {
        switch (theRelation) {
            case "educations":
                educationService.deleteInCvBody(cvId, id);
                break;
            case "skills":
                skillService.deleteInCvBody(cvId, id);
                break;
            case "experiences":
                experienceService.deleteInCvBody(cvId, id);
                break;
            case "involvements":
                involvementService.deleteInCvBody(cvId, id);
                break;
            case "projects":
                projectService.deleteInCvBody(cvId, id);
                break;
            case "certifications":
                certificationService.deleteInCvBody(cvId, id);
                break;
            default:
                throw new Exception("Invalid request!!");
        }
        return "Delete successful";
    }

    @GetMapping("/synchUp/{cvId}")
    public CvDto synchUp(@PathVariable("cvId") int cvId) throws JsonProcessingException {
        return cvService.synchUp(cvId);
    }

}
