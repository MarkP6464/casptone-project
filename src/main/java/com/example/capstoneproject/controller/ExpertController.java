package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.ExpertUpdateDto;
import com.example.capstoneproject.Dto.responses.ExpertViewChooseDto;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.service.ExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> updateExpert(@PathVariable("expert-id") Integer expertId, ExpertUpdateDto dto) {
        if (expertService.updateExpert(expertId, dto)) {
            return ResponseEntity.ok("Update success");
        } else {
            return ResponseEntity.badRequest().body("Update failed");
        }
    }

    @GetMapping("/expert/{expert-id}")
    public ResponseEntity<?> getExpert(@PathVariable("expert-id") Integer expertId){
        return ResponseEntity.ok(expertService.getDetailExpert(expertId));
    }

    @GetMapping("/experts")
    public ResponseEntity<?> getAllExpert(@RequestParam(required = false) String search) {
        try {
            List<ExpertViewChooseDto> expertList = expertService.getExpertList(search);
            return ResponseEntity.ok(expertList);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
