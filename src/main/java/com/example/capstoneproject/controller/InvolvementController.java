package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.ExperienceDto;
import com.example.capstoneproject.Dto.InvolvementDto;
import com.example.capstoneproject.service.ExperienceService;
import com.example.capstoneproject.service.InvolvementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cv/involvements")
public class InvolvementController {
    @Autowired
    InvolvementService involvementService;

    public InvolvementController(InvolvementService involvementService) {
        this.involvementService = involvementService;
    }

    @GetMapping
    public List<InvolvementDto> getAllInvolvement() {
        return involvementService.getAll();
    }

    @PostMapping
    public InvolvementDto postInvolvement(@RequestBody InvolvementDto Dto) {
        return involvementService.create(Dto);
    }

    @PutMapping("/{involvementId}")
    public InvolvementDto updateInvolvement(@PathVariable("involvementId") int involvementId, @RequestBody InvolvementDto Dto) {
        return involvementService.update(involvementId, Dto);
    }

    @DeleteMapping("/{involvementId}")
    public String deleteInvolvement(@PathVariable("involvementId") int involvementId) {
        involvementService.deleteById(involvementId);
        return "Delete successful";
    }
}
