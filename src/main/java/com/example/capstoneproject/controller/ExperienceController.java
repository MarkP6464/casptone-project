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
@RequestMapping("/api/v1/customer")
public class ExperienceController {
    @Autowired
    ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @GetMapping("/{customerId}/experiences")
    public List<ExperienceViewDto> getAllExperience(@PathVariable("customerId") int customerId) {
        return experienceService.getAllExperience(customerId);
    }

    @PostMapping("/{customerId}/experiences")
    public ExperienceDto postExperience(@PathVariable("customerId") int customerId, @RequestBody ExperienceDto Dto) {
        return experienceService.createExperience(customerId,Dto);
    }

    @PutMapping("/{customerId}/experiences/{experienceId}")
    public String updateExperience(@PathVariable("customerId") int customerId,@PathVariable("experienceId") int experienceId, @RequestBody ExperienceDto Dto) {
        boolean check = experienceService.updateExperience(customerId,experienceId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{customerId}/experiences/{experienceId}")
    public String deleteExperience(@PathVariable("customerId") int customerId,@PathVariable("experienceId") int experienceId) {
        experienceService.deleteExperienceById(customerId,experienceId);
        return "Delete successful";
    }
}
