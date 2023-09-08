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

    @PostMapping("/{cvId}/involvements")
    public InvolvementDto postInvolvement(@PathVariable("cvId") int cvId,@RequestBody InvolvementDto Dto) {
        return involvementService.createInvolvement(cvId,Dto);
    }

    @PutMapping("/{cvId}/involvements/{involvementId}")
    public String updateInvolvement(@PathVariable("cvId") int cvId,@PathVariable("involvementId") int involvementId, @RequestBody InvolvementDto Dto) {
        boolean check = involvementService.updateInvolvement(cvId,involvementId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{cvId}/involvements/{involvementId}")
    public String deleteInvolvement(@PathVariable("cvId") int cvId,@PathVariable("involvementId") int involvementId) {
        involvementService.deleteInvolvementById(cvId,involvementId);
        return "Delete successful";
    }
}
