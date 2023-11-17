package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.NoteDto;
import com.example.capstoneproject.service.ApplicationLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ApplicationLogController {

    @Autowired
    ApplicationLogService applicationLogService;

    public ApplicationLogController(ApplicationLogService applicationLogService) {
        this.applicationLogService = applicationLogService;
    }

    @PostMapping("/user/{user-id}/cv/{cv-id}/job-posting/{posting-id}/apply")
    public ResponseEntity<?> createApplication(@PathVariable("user-id") Integer userId, @PathVariable("cv-id") Integer cvId, @PathVariable("posting-id") Integer postingId, @RequestParam(required = false) Integer cover_letter_id, NoteDto dto) throws JsonProcessingException {
        if (cover_letter_id == null) {
            if (applicationLogService.applyCvToPost(userId, cvId, null, postingId, dto)) {
                return ResponseEntity.ok("Apply success");
            } else {
                return ResponseEntity.badRequest().body("Apply failed");
            }
        }
        if (applicationLogService.applyCvToPost(userId, cvId, cover_letter_id, postingId, dto)) {
            return ResponseEntity.ok("Apply success");
        } else {
            return ResponseEntity.badRequest().body("Apply failed");
        }
    }
}
