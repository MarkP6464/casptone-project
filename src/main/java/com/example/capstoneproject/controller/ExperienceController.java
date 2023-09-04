package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.EducationDto;
import com.example.capstoneproject.Dto.ExperienceDto;
import com.example.capstoneproject.service.EducationService;
import com.example.capstoneproject.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cv/experiences")
public class ExperienceController {
    @Autowired
    ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @GetMapping
    public List<ExperienceDto> getAllExperience() {
        return experienceService.getAll();
    }

    @PostMapping
    public ExperienceDto postExperience(@RequestBody ExperienceDto Dto) {
        return experienceService.create(Dto);
    }

    @PutMapping("/{experienceId}")
    public ExperienceDto updateExperience(@PathVariable("experienceId") int experienceId, @RequestBody ExperienceDto Dto) {
        return experienceService.update(experienceId, Dto);
    }

    @DeleteMapping("/{experienceId}")
    public String deleteExperience(@PathVariable("experienceId") int experienceId) {
        experienceService.deleteById(experienceId);
        return "Delete successful";
    }
}
