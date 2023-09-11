package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.EducationDto;
import com.example.capstoneproject.Dto.EducationViewDto;
import com.example.capstoneproject.service.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
public class EducationController {
    @Autowired
    EducationService educationService;

    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @GetMapping("/{customerId}/educations")
    public List<EducationViewDto> getAllEducation(@PathVariable("customerId") int customerId) {
        return educationService.getAllEducation(customerId);
    }

    @PostMapping("/{customerId}/educations")
    public EducationDto postEducation(@PathVariable("customerId") int customerId,@RequestBody EducationDto Dto) {
        return educationService.createEducation(customerId,Dto);
    }

    @PutMapping("/{customerId}/educations/{educationId}")
    public String updateEducation(@PathVariable("customerId") int customerId,@PathVariable("educationId") int educationId, @RequestBody EducationDto Dto) {
        boolean check = educationService.updateEducation(customerId,educationId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{customerId}/educations/{educationId}")
    public String deleteCertification(@PathVariable("customerId") int customerId,@PathVariable("educationId") int educationId) {
        educationService.deleteEducationById(customerId,educationId);
        return "Delete successful";
    }
}
