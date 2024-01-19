package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    CustomSectionService customSectionService;

    @Autowired
    ObjectMapper objectMapper;

    @PutMapping(value = "/{cvId}/{theRelation}/update-all", consumes = "application/json")
    @PreAuthorize("hasAnyAuthority('create:candidate','create:candidate')")
    public ResponseEntity<?> post(HttpServletRequest request, @PathVariable("cvId") int cvId, @PathVariable("theRelation") String theRelation, @RequestBody List<Object> list) throws Exception {

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
                break;
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
                break;
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
                break;
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
                break;
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
                break;
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
                break;
            default:
                throw new Exception("Invalid request!!");
        }
        Cv cv = cvService.getCvById(cvId);
        cvService.saveAfterFiveMin(request, cvId, cv.deserialize());
        return ResponseEntity.ok("Update the orders successfully");
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
    public String post(HttpServletRequest request, @PathVariable("cvId") int cvId, @PathVariable("theRelation") String theRelation, @Valid @RequestBody Object obj) throws Exception {

        switch (theRelation) {
            case "educations":
                EducationDto educationDto = objectMapper.convertValue(obj, EducationDto.class);
                educationService.createOfUserInCvBody(cvId, educationDto);
                break;
            case "skills":
                SkillDto skillDto = objectMapper.convertValue(obj, SkillDto.class);
                skillService.createOfUserInCvBody(cvId, skillDto);
                break;
            case "experiences":
                ExperienceDto experienceDto = objectMapper.convertValue(obj, ExperienceDto.class);
                experienceService.createOfUserInCvBody(cvId, experienceDto);
                break;
            case "involvements":
                InvolvementDto involvementDto = objectMapper.convertValue(obj, InvolvementDto.class);
                involvementService.createOfUserInCvBody(cvId, involvementDto);
                break;
            case "projects":
                ProjectDto projectDto = objectMapper.convertValue(obj, ProjectDto.class);
                projectService.createOfUserInCvBody(cvId, projectDto);
                break;
            case "certifications":
                CertificationDto certificationDto = objectMapper.convertValue(obj, CertificationDto.class);
                certificationService.createOfUserInCvBody(cvId, certificationDto);
                break;
            default:
                throw new Exception("Invalid request!!");
        }
        Cv cv = cvService.getCvById(cvId);
        cvService.saveAfterFiveMin(request, cvId, cv.deserialize());
        return "Create successfull";
    }

    @PutMapping("/{cvId}/{theRelation}/{id}")
    @PreAuthorize("hasAnyAuthority('update:candidate','update:expert')")
    public String update(HttpServletRequest request, @PathVariable("cvId") int cvId, @PathVariable("id") int id, @PathVariable("theRelation") String theRelation, @RequestBody Object obj) throws Exception {
        switch (theRelation) {
            case "educations":
                EducationDto educationDto = objectMapper.convertValue(obj, EducationDto.class);
                educationService.updateInCvBody(cvId, id, educationDto);
                break;
            case "skills":
                SkillDto skillDto = objectMapper.convertValue(obj, SkillDto.class);
                skillService.updateInCvBody(cvId, id, skillDto);
                break;
            case "experiences":
                ExperienceDto experienceDto = objectMapper.convertValue(obj, ExperienceDto.class);
                experienceService.updateInCvBody(cvId, id, experienceDto);
                break;
            case "involvements":
                InvolvementDto involvementDto = objectMapper.convertValue(obj, InvolvementDto.class);
                involvementService.updateInCvBody(cvId, id, involvementDto);
                break;
            case "projects":
                ProjectDto projectDto = objectMapper.convertValue(obj, ProjectDto.class);
                projectService.updateInCvBody(cvId, id, projectDto);
                break;
            case "certifications":
                CertificationDto certificationDto = objectMapper.convertValue(obj, CertificationDto.class);
                certificationService.updateInCvBody(cvId, id, certificationDto);
                break;
            default:
                throw new Exception("Invalid request!!");
        }
        Cv cv = cvService.getCvById(cvId);
        cvService.saveAfterFiveMin(request, cvId, cv.deserialize());
        return "Update successfull";
    }

    @DeleteMapping("/{cvId}/{theRelation}/{id}")
    @PreAuthorize("hasAnyAuthority('delete:candidate','delete:expert')")
    public String deleteARelation(HttpServletRequest request, @PathVariable("cvId") int cvId, @PathVariable("theRelation") String theRelation, @PathVariable("id") int id) throws Exception {

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
        Cv cv = cvService.getCvById(cvId);
        cvService.saveAfterFiveMin(request, cvId, cv.deserialize());
        return "Delete successful";
    }

    @GetMapping("/synchUp/{cvId}")
    public CvDto synchUp(@PathVariable("cvId") int cvId) throws JsonProcessingException {
        return cvService.synchUp(cvId);
    }


    @PostMapping("/{cv-id}/parse/{history-id}")
    @PreAuthorize("hasAnyAuthority('read:candidate', 'read:expert')")
    public ResponseEntity<?> parse(@PathVariable("cv-id") int cvId, @PathVariable("history-id") int historyId) throws Exception {
        return ResponseEntity.ok(cvService.parse(cvId, historyId));
    }
}
