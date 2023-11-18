package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.ExpertTurnOnDto;
import com.example.capstoneproject.Dto.ExpertUpdateDto;
import com.example.capstoneproject.Dto.responses.ExpertViewChooseDto;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.service.ExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ExpertController {

    @Autowired
    ExpertService expertService;

    public ExpertController(ExpertService expertService) {
        this.expertService = expertService;
    }

    @PutMapping("/expert/{expert-id}")
    @PreAuthorize("hasAuthority('update:expert')")
    public ResponseEntity<?> updateExpert(@PathVariable("expert-id") Integer expertId, ExpertUpdateDto dto) {
        if (expertService.updateExpert(expertId, dto)) {
            return ResponseEntity.ok("Update success");
        } else {
            return ResponseEntity.badRequest().body("Update failed");
        }
    }

    @GetMapping("/expert/{expert-id}")
    @PreAuthorize("hasAuthority('read:expert')")
    public ResponseEntity<?> getExpert(@PathVariable("expert-id") Integer expertId){
        return ResponseEntity.ok(expertService.getDetailExpert(expertId));
    }

    @GetMapping("/experts")
    @PreAuthorize("hasAuthority('read:candidate')")
    public ResponseEntity<?> getAllExpert(@RequestParam(required = false) String search) {
        try {
            List<ExpertViewChooseDto> expertList = expertService.getExpertList(search);
            return ResponseEntity.ok(expertList);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/expert/{expert-id}/availability/turn-off")
    @PreAuthorize("hasAuthority('create:expert')")
    public ResponseEntity<String> turnOffAvailability(@PathVariable("expert-id") Integer expertId) {
        boolean success = expertService.turnOffAvailability(expertId);
        if (success) {
            return ResponseEntity.ok("Availability turned off for expert with ID: " + expertId);
        } else {
            return ResponseEntity.badRequest().body("Failed to turn off availability for expert with ID: " + expertId);
        }
    }

    @PostMapping("/expert/{expert-id}/availability/turn-on")
    @PreAuthorize("hasAuthority('create:expert')")
    public ResponseEntity<String> turnOnAvailability(@PathVariable("expert-id") Integer expertId, ExpertTurnOnDto dto) {
        boolean success = expertService.turnOnAvailability(expertId, dto);
        if (success) {
            return ResponseEntity.ok("Availability turned on for expert with ID: " + expertId);
        } else {
            return ResponseEntity.badRequest().body("Failed to turn on availability for expert with ID: " + expertId);
        }
    }
}
