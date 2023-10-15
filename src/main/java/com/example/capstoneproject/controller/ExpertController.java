package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.ExpertUpdateDto;
import com.example.capstoneproject.service.ExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/expert")
public class ExpertController {

    @Autowired
    ExpertService expertService;

    public ExpertController(ExpertService expertService) {
        this.expertService = expertService;
    }

    @PutMapping("/{expertId}")
    public ResponseEntity<?> updateExpert(@PathVariable("expertId") Integer expertId, ExpertUpdateDto dto) {
        if (expertService.updateExpert(expertId, dto)) {
            return ResponseEntity.ok("Update success");
        } else {
            return ResponseEntity.badRequest().body("Update failed");
        }
    }

    @GetMapping("/{expertId}")
    public ResponseEntity<?> getExpert(@PathVariable("expertId") Integer expertId){
        return ResponseEntity.ok(expertService.getProfileExpert(expertId));
    }
}
