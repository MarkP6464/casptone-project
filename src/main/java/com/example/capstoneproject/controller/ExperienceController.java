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

    @PostMapping("/experiences")
    public ExperienceDto postExperience(@RequestBody ExperienceDto Dto) {
        return experienceService.create(Dto);
    }

    @PutMapping("/experiences/{experienceId}")
    public String updateExperience(@PathVariable("experienceId") int experienceId, @RequestBody ExperienceViewDto Dto) {
        boolean check = experienceService.updateExperience(experienceId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/experiences/{experienceId}")
    public String deleteExperience(@PathVariable("experienceId") int experienceId) {
        experienceService.deleteById(experienceId);
        return "Delete successful";
    }
}
