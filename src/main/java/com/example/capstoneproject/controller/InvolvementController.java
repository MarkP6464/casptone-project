package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.ExperienceDto;
import com.example.capstoneproject.Dto.InvolvementDto;
import com.example.capstoneproject.Dto.InvolvementViewDto;
import com.example.capstoneproject.service.ExperienceService;
import com.example.capstoneproject.service.InvolvementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cv")
public class InvolvementController {
    @Autowired
    InvolvementService involvementService;

    public InvolvementController(InvolvementService involvementService) {
        this.involvementService = involvementService;
    }

    @GetMapping("/{cvId}/involvements")
    public List<InvolvementViewDto> getAllInvolvement(@PathVariable("cvId") int cvId) {
        return involvementService.getAllInvolvement(cvId);
    }

    @PostMapping("/involvements")
    public InvolvementDto postInvolvement(@RequestBody InvolvementDto Dto) {
        return involvementService.create(Dto);
    }

    @PutMapping("/involvements/{involvementId}")
    public String updateInvolvement(@PathVariable("involvementId") int involvementId, @RequestBody InvolvementViewDto Dto) {
        boolean check = involvementService.updateInvolvement(involvementId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/involvements/{involvementId}")
    public String deleteInvolvement(@PathVariable("involvementId") int involvementId) {
        involvementService.deleteById(involvementId);
        return "Delete successful";
    }
}
