package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.InvolvementViewDto;
import com.example.capstoneproject.service.InvolvementOfCvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cv")
public class InvolvementOfCvController {
    @Autowired
    InvolvementOfCvService involvementOfCvService;

    public InvolvementOfCvController(InvolvementOfCvService involvementOfCvService) {
        this.involvementOfCvService = involvementOfCvService;
    }

    @GetMapping("/{cvId}/involvements/involvement-of-cv")
    public List<InvolvementViewDto> getAllInvolvement(@PathVariable("cvId") int cvId) {
        return involvementOfCvService.getActiveInvolvementsByCvId(cvId);
    }
    @GetMapping("/{cvId}/involvements")
    public List<InvolvementViewDto> getAllInvolvementNotYet(@PathVariable("cvId") int cvId) {
        return involvementOfCvService.getAllInvolvement(cvId);
    }
    @PostMapping("/{cvId}/involvements/{involvementId}")
    public String postInvolvementOfCv(@PathVariable("cvId") int cvId, @PathVariable("involvementId") int involvementId) {
        boolean check = involvementOfCvService.createInvolvementOfCv(cvId,involvementId);
        if(check){
            return "Addition success";
        }else {
            return "Addition fail";
        }
    }

    @DeleteMapping("/{cvId}/involvements/{involvementId}")
    public String deleteInvolvementOfCv(@PathVariable("cvId") int cvId, @PathVariable("involvementId") int involvementId) {
        boolean check = involvementOfCvService.deleteInvolvementOfCv(cvId,involvementId);
        if(check){
            return "Delete success";
        }else {
            return "Delete fail";
        }
    }
}
