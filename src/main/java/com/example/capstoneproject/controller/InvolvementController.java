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
@RequestMapping("/api/v1/Users")
public class InvolvementController {
    @Autowired
    InvolvementService involvementService;

    public InvolvementController(InvolvementService involvementService) {
        this.involvementService = involvementService;
    }

    @GetMapping("/{UsersId}/involvements")
    public List<InvolvementViewDto> getAllInvolvement(@PathVariable("UsersId") int UsersId) {
        return involvementService.getAllInvolvement(UsersId);
    }

    @PostMapping("/{UsersId}/involvements")
    public InvolvementDto postInvolvement(@PathVariable("UsersId") int UsersId,@RequestBody InvolvementDto Dto) {
        return involvementService.createInvolvement(UsersId,Dto);
    }

    @PutMapping("/{UsersId}/involvements/{involvementId}")
    public String updateInvolvement(@PathVariable("UsersId") int UsersId,@PathVariable("involvementId") int involvementId, @RequestBody InvolvementDto Dto) {
        boolean check = involvementService.updateInvolvement(UsersId,involvementId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{UsersId}/involvements/{involvementId}")
    public String deleteInvolvement(@PathVariable("UsersId") int UsersId,@PathVariable("involvementId") int involvementId) {
        involvementService.deleteInvolvementById(UsersId,involvementId);
        return "Delete successful";
    }
}
