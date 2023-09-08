package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.EducationDto;
import com.example.capstoneproject.Dto.EducationViewDto;
import com.example.capstoneproject.service.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cv")
public class EducationController {
    @Autowired
    EducationService educationService;

    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @GetMapping("/{cvId}/educations")
    public List<EducationViewDto> getAllEducation(@PathVariable("cvId") int cvId) {
        return educationService.getAllEducation(cvId);
    }

    @PostMapping("/{cvId}/educations")
    public EducationDto postEducation(@PathVariable("cvId") int cvId,@RequestBody EducationDto Dto) {
        return educationService.createEducation(cvId,Dto);
    }

    @PutMapping("/{cvId}/educations/{educationId}")
    public String updateEducation(@PathVariable("cvId") int cvId,@PathVariable("educationId") int educationId, @RequestBody EducationDto Dto) {
        boolean check = educationService.updateEducation(cvId,educationId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{cvId}/educations/{educationId}")
    public String deleteCertification(@PathVariable("cvId") int cvId,@PathVariable("educationId") int educationId) {
        educationService.deleteEducationById(cvId,educationId);
        return "Delete successful";
    }
}
