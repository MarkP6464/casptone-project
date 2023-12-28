package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.InvolvementDto;
import com.example.capstoneproject.service.InvolvementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/Users")
public class InvolvementController {
    @Autowired
    InvolvementService involvementService;

    @GetMapping("/{UsersId}/involvements")
    public List<InvolvementDto> getAllInvolvement(@PathVariable("UsersId") int UsersId) {
        return involvementService.getAllInvolvement(UsersId);
    }

    @PostMapping("/cv/{cv-id}/involvements")
    public InvolvementDto postInvolvement(@PathVariable("cv-id") int cvId,@RequestBody InvolvementDto Dto) {
        return involvementService.createInvolvement(cvId,Dto);
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
