package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.EducationDto;
import com.example.capstoneproject.Dto.responses.EducationViewDto;
import com.example.capstoneproject.service.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/Users")
public class EducationController {
    @Autowired
    EducationService educationService;

    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @GetMapping("/{UsersId}/educations")
    public List<EducationDto> getAllEducation(@PathVariable("UsersId") int UsersId) {
        return educationService.getAllEducation(UsersId);
    }

    @PostMapping("/{UsersId}/educations")
    public EducationDto postEducation(@PathVariable("UsersId") int UsersId,@RequestBody EducationDto Dto) {
        return educationService.createEducation(UsersId,Dto);
    }

    @PutMapping("/{UsersId}/educations/{educationId}")
    public String updateEducation(@PathVariable("UsersId") int UsersId,@PathVariable("educationId") int educationId, @RequestBody EducationDto Dto) {
        boolean check = educationService.updateEducation(UsersId,educationId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{UsersId}/educations/{educationId}")
    public String deleteCertification(@PathVariable("UsersId") int UsersId,@PathVariable("educationId") int educationId) {
        educationService.deleteEducationById(UsersId,educationId);
        return "Delete successful";
    }
}
