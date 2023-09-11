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
@RequestMapping("/api/v1/customer")
public class InvolvementController {
    @Autowired
    InvolvementService involvementService;

    public InvolvementController(InvolvementService involvementService) {
        this.involvementService = involvementService;
    }

    @GetMapping("/{customerId}/involvements")
    public List<InvolvementViewDto> getAllInvolvement(@PathVariable("customerId") int customerId) {
        return involvementService.getAllInvolvement(customerId);
    }

    @PostMapping("/{customerId}/involvements")
    public InvolvementDto postInvolvement(@PathVariable("customerId") int customerId,@RequestBody InvolvementDto Dto) {
        return involvementService.createInvolvement(customerId,Dto);
    }

    @PutMapping("/{customerId}/involvements/{involvementId}")
    public String updateInvolvement(@PathVariable("customerId") int customerId,@PathVariable("involvementId") int involvementId, @RequestBody InvolvementDto Dto) {
        boolean check = involvementService.updateInvolvement(customerId,involvementId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{customerId}/involvements/{involvementId}")
    public String deleteInvolvement(@PathVariable("customerId") int customerId,@PathVariable("involvementId") int involvementId) {
        involvementService.deleteInvolvementById(customerId,involvementId);
        return "Delete successful";
    }
}
