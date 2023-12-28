package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.ExperienceDto;
import com.example.capstoneproject.Dto.responses.ExperienceViewDto;
import com.example.capstoneproject.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class ExperienceController {
    @Autowired
    ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @GetMapping("/cv/{cv-id}/experiences")
    public List<ExperienceDto> getAllExperience(@PathVariable("cv-id") int cvId) {
        return experienceService.getAllExperience(cvId);
    }

    @PostMapping("/{UsersId}/experiences")
    public ExperienceViewDto postExperience(@PathVariable("UsersId") int UsersId, @RequestBody ExperienceDto Dto) {
        return experienceService.createExperience(UsersId,Dto);
    }

    @PutMapping("/cv/{cv-id}/experiences/{experience-id}")
    public String updateExperience(@PathVariable("cv-id") int cvId,@PathVariable("experience-id") int experienceId, @RequestBody ExperienceDto Dto) {
        boolean check = experienceService.updateExperience(cvId,experienceId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{UsersId}/experiences/{experienceId}")
    public String deleteExperience(@PathVariable("UsersId") int UsersId,@PathVariable("experienceId") int experienceId) {
        experienceService.deleteExperienceById(UsersId,experienceId);
        return "Delete successful";
    }
}
