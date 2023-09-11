package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.EducationViewDto;
import com.example.capstoneproject.service.EducationOfCvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cv")
public class EducationOfCvController {
    @Autowired
    EducationOfCvService educationOfCvService;

    public EducationOfCvController(EducationOfCvService educationOfCvService) {
        this.educationOfCvService = educationOfCvService;
    }

    @GetMapping("/{cvId}/educations/education-of-cv")
    public List<EducationViewDto> getAllEducation(@PathVariable("cvId") int cvId) {
        return educationOfCvService.getActiveEducationsByCvId(cvId);
    }
    @GetMapping("/{cvId}/educations")
    public List<EducationViewDto> getAllEducationNotYet(@PathVariable("cvId") int cvId) {
        return educationOfCvService.getAllEducation(cvId);
    }
    @PostMapping("/{cvId}/educations/{educationId}")
    public String postEducationOfCv(@PathVariable("cvId") int cvId, @PathVariable("educationId") int educationId) {
        boolean check = educationOfCvService.createEducationOfCv(cvId,educationId);
        if(check){
            return "Addition success";
        }else {
            return "Addition fail";
        }
    }

    @DeleteMapping("/{cvId}/educations/{educationId}")
    public String deleteEducationOfCv(@PathVariable("cvId") int cvId, @PathVariable("educationId") int educationId) {
        boolean check = educationOfCvService.deleteEducationOfCv(cvId,educationId);
        if(check){
            return "Delete success";
        }else {
            return "Delete fail";
        }
    }
}
