package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.EducationDto;
import com.example.capstoneproject.service.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cv/educations")
public class EducationController {
    @Autowired
    EducationService educationService;

    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @GetMapping
    public List<EducationDto> getAllEducation() {
        return educationService.getAll();
    }

    @PostMapping
    public EducationDto postEducation(@RequestBody EducationDto Dto) {
        return educationService.create(Dto);
    }

    @PutMapping("/{educationId}")
    public EducationDto updateEducation(@PathVariable("educationId") int educationId, @RequestBody EducationDto Dto) {
        return educationService.update(educationId, Dto);
    }

    @DeleteMapping("/{educationId}")
    public String deleteCertification(@PathVariable("educationId") int educationId) {
        educationService.deleteById(educationId);
        return "Delete successful";
    }
}
