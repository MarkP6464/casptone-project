package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.ChatResponse;
import com.example.capstoneproject.Dto.ChatResponseArray;
import com.example.capstoneproject.Dto.ReWritterExperienceDto;
import com.example.capstoneproject.Dto.SummaryGenerationDto;
import com.example.capstoneproject.service.CvService;
//import com.example.capstoneproject.service.impl.Auth0Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/chat-gpt")
public class ChatGptController {

    @Autowired
    CvService cvService;


    @PostMapping("/cv/{cv-id}/summary")
    @PreAuthorize("hasAnyAuthority('create:candidate','create:expert')")
    public ResponseEntity<?> generateSummary(
            @PathVariable("cv-id") Integer cvId,
            @RequestBody SummaryGenerationDto dto,
            Principal principal
    ) throws JsonProcessingException {
        ChatResponse result = cvService.generateSummaryCV(
                cvId,
                dto,
                principal
        );
        return ResponseEntity.ok(result);
    }

    @PostMapping("/cv/experience/re-writer")
    @PreAuthorize("hasAnyAuthority('create:candidate','create:expert')")
    public ResponseEntity<?> rewriteExperience(
            @RequestBody ReWritterExperienceDto dto,
            Principal principal
    ) throws JsonProcessingException {
        ChatResponseArray result = cvService.rewritteExperience(
                dto,
                principal
        );
        return ResponseEntity.ok(result);
    }

    @PostMapping("/cv/{cv-id}/review")
    @PreAuthorize("hasAnyAuthority('create:candidate','create:expert')")
    public ResponseEntity<?> reviewCv(
            @RequestParam float temperature,
            @PathVariable("cv-id") Integer cvId,
            Principal principal
    ) throws JsonProcessingException {
        if (temperature < 0.2 || temperature > 1.0) {
            return ResponseEntity.badRequest().body("Temperature value is invalid. Must be between 0.2 and 1.0.");
        }

        ChatResponse result = cvService.reviewCV(
                temperature,
                cvId,
                principal
        );
        return ResponseEntity.ok(result);
    }


//    @Autowired
//    private Auth0Service auth0Service;
//
//    @PostMapping("/add-role")
//    public void addRoleToUser(@RequestParam String userId, @RequestParam String roleName) {
//        auth0Service.addRoleToUser(userId, roleName);
//    }
//
//    @PostMapping("/add-role1")
//    public void addRoleToUser1(@RequestParam String userId, @RequestParam String roleName, @RequestParam String token) {
//        auth0Service.addRoleToUser1(userId, roleName, token);
//    }
//    @PostMapping("/add-role12")
//    public void addRoleToUser2( @RequestParam String token) {
//        auth0Service.addRoleToUser2(token);
//    }
}
