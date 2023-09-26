package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.EducationDto;
import com.example.capstoneproject.Dto.EducationViewDto;
import com.example.capstoneproject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/v1/cv")
public class CvRelationController {
    @Autowired
    EducationService educationService;

    @Autowired
    SkillService skillService;

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

    @GetMapping("/{cvId}/{theRelation}")
    public List<?> getAllEducation(@PathVariable("cvId") int cvId, @PathVariable("theRelation") String theRelation) throws Exception {

        switch (theRelation){
            case "education":
                return educationService.getAllEducation(cvId);
            case "skill":
                return skillService.getAllSkill(cvId);
            case "experience":
                return experienceService.getAllExperience(cvId);
            case "involvement":
                return involvementService.getAllInvolvement(cvId);
            case "project":
                return projectService.getAllProject(cvId);
            case "sourework":
                return sourceWorkService.getAllSourceWork(cvId);
            case "certification":
                return certificationService.getAllCertification(cvId);
            default:
                throw new Exception("Invalid request!!");
        }
    }

    @PostMapping("/{cvId}/{theRelation}")
    public EducationDto postEducation(@PathVariable("cvId") int cvId, @PathVariable("theRelation") String theRelation, @RequestBody EducationDto Dto) {
        return educationService.createEducation(cvId,Dto);
    }

    @PutMapping("/{cvId}/{theRelation}/{id}")
    public String updateEducation(@PathVariable("cvId") int cvId,@PathVariable("id") int id, @PathVariable("theRelation") String theRelation, @RequestBody EducationDto Dto) {
        boolean check = educationService.updateEducation(cvId,id, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{cvId}/{theRelation}/{id}")
    public String deleteCertification(@PathVariable("cvId") int cvId, @PathVariable("theRelation") String theRelation,@PathVariable("id") int id) {
        educationService.deleteEducationById(cvId,id);
        return "Delete successful";
    }


}
