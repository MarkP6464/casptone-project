package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.service.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;


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
    SourceWorkService sourceWorkService;
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


    @GetMapping("/{cvId}/{theRelation}")
    public Set<?> getAllARelation(@PathVariable("cvId") int cvId, @PathVariable("theRelation") String theRelation) throws Exception {

        switch (theRelation){
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
            case "soureworks":
                return sourceWorkService.getAllARelationInCvBody(cvId);
            case "certifications":
                return certificationService.getAllARelationInCvBody(cvId);
            default:
                throw new Exception("Invalid request!!");
        }
    }

    @GetMapping("/{cvId}/{theRelation}/{id}")
    public ResponseEntity<?> getARelation(@PathVariable("cvId") int cvId, @PathVariable("theRelation") String theRelation, @PathVariable("id") int id) throws Exception {

        switch (theRelation){
            case "educations":
                return ResponseEntity.ok(educationService.getAndIsDisplay(cvId, id));
            case "skills":
                return ResponseEntity.ok(skillService.getAndIsDisplay(cvId, id));
            case "soureworks":
                return ResponseEntity.ok(sourceWorkService.getAndIsDisplay(cvId, id));
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

    @PostMapping("/{cvId}/{theRelation}")
    public ResponseEntity<?> postEducation(@PathVariable("cvId") int cvId, @PathVariable("theRelation") String theRelation, @RequestBody Object obj) throws Exception {

        switch (theRelation){
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
            case "soureworks":
                SourceWorkDto sourceWorkDto = objectMapper.convertValue(obj, SourceWorkDto.class);
                return ResponseEntity.ok(sourceWorkService.createOfUserInCvBody(cvId, sourceWorkDto));
            case "certifications":
                CertificationDto certificationDto = objectMapper.convertValue(obj, CertificationDto.class);
                return ResponseEntity.ok(certificationService.createOfUserInCvBody(cvId, certificationDto));
            default:
                throw new Exception("Invalid request!!");
        }
    }

    @PutMapping("/{cvId}/{theRelation}/{id}")
    public String updateEducation(@PathVariable("cvId") int cvId,@PathVariable("id") int id, @PathVariable("theRelation") String theRelation, @RequestBody Object obj) throws Exception {
        switch (theRelation){
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
            case "soureworks":
                SourceWorkDto sourceWorkDto = objectMapper.convertValue(obj, SourceWorkDto.class);
                sourceWorkService.updateInCvBody(cvId, id, sourceWorkDto);
                break;
            case "certifications":
                CertificationDto certificationDto = objectMapper.convertValue(obj, CertificationDto.class);
                certificationService.updateInCvBody(cvId, id, certificationDto);
                break;
            default:
                throw new Exception("Invalid request!!");
        }
        return "Changes saved";
    }

    @DeleteMapping("/{cvId}/{theRelation}/{id}")
    public String deleteCertification(@PathVariable("cvId") int cvId, @PathVariable("theRelation") String theRelation,@PathVariable("id") int id) throws Exception {
        switch (theRelation){
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
            case "soureworks":
                 sourceWorkService.deleteInCvBody(cvId, id);
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
