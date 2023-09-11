package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.ExperienceViewDto;
import com.example.capstoneproject.service.ExperienceOfCvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cv")
public class ExperienceOfCvController {
    @Autowired
    ExperienceOfCvService experienceOfCvService;

    public ExperienceOfCvController(ExperienceOfCvService experienceOfCvService) {
        this.experienceOfCvService = experienceOfCvService;
    }

    @GetMapping("/{cvId}/experiences/experience-of-cv")
    public List<ExperienceViewDto> getAllExperience(@PathVariable("cvId") int cvId) {
        return experienceOfCvService.getActiveExperiencesByCvId(cvId);
    }
    @GetMapping("/{cvId}/experiences")
    public List<ExperienceViewDto> getAllExperienceNotYet(@PathVariable("cvId") int cvId) {
        return experienceOfCvService.getAllExperience(cvId);
    }
    @PostMapping("/{cvId}/experiences/{experienceId}")
    public String postExperienceOfCv(@PathVariable("cvId") int cvId, @PathVariable("experienceId") int experienceId) {
        boolean check = experienceOfCvService.createExperienceOfCv(cvId,experienceId);
        if(check){
            return "Addition success";
        }else {
            return "Addition fail";
        }
    }

    @DeleteMapping("/{cvId}/experiences/{experienceId}")
    public String deleteExperienceOfCv(@PathVariable("cvId") int cvId, @PathVariable("experienceId") int experienceId) {
        boolean check = experienceOfCvService.deleteExperienceOfCv(cvId,experienceId);
        if(check){
            return "Delete success";
        }else {
            return "Delete fail";
        }
    }
}
