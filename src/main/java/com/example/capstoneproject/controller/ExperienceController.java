package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.EducationDto;
import com.example.capstoneproject.Dto.ExperienceDto;
import com.example.capstoneproject.Dto.ExperienceViewDto;
import com.example.capstoneproject.service.EducationService;
import com.example.capstoneproject.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cv")
public class ExperienceController {
    @Autowired
    ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @GetMapping("/{cvId}/experiences")
    public List<ExperienceViewDto> getAllExperience(@PathVariable("cvId") int cvId) {
        return experienceService.getAllExperience(cvId);
    }

    @PostMapping("/{cvId}/experiences")
    public ExperienceDto postExperience(@PathVariable("cvId") int cvId, @RequestBody ExperienceDto Dto) {
        return experienceService.createExperience(cvId,Dto);
    }

    @PutMapping("/{cvId}/experiences/{experienceId}")
    public String updateExperience(@PathVariable("cvId") int cvId,@PathVariable("experienceId") int experienceId, @RequestBody ExperienceDto Dto) {
        boolean check = experienceService.updateExperience(cvId,experienceId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{cvId}/experiences/{experienceId}")
    public String deleteExperience(@PathVariable("cvId") int cvId,@PathVariable("experienceId") int experienceId) {
        experienceService.deleteExperienceById(cvId,experienceId);
        return "Delete successful";
    }
}
