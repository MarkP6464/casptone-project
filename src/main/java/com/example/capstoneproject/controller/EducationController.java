package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.EducationDto;
import com.example.capstoneproject.Dto.EducationViewDto;
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

    @GetMapping("/{cvId}")
    public List<EducationViewDto> getAllEducation(@PathVariable("cvId") int cvId) {
        return educationService.getAllEducation(cvId);
    }

    @PostMapping
    public EducationDto postEducation(@RequestBody EducationDto Dto) {
        return educationService.create(Dto);
    }

    @PutMapping("/{educationId}")
    public String updateEducation(@PathVariable("educationId") int educationId, @RequestBody EducationViewDto Dto) {
        boolean check = educationService.updateEducation(educationId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{educationId}")
    public String deleteCertification(@PathVariable("educationId") int educationId) {
        educationService.deleteById(educationId);
        return "Delete successful";
    }
}
