package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.EvaluateDescriptionDto;
import com.example.capstoneproject.Dto.ScoreDto;
import com.example.capstoneproject.service.CvService;
import com.example.capstoneproject.service.EvaluateService;
import com.example.capstoneproject.service.impl.ChatGPTServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class EvaluateController {

    @Autowired
    EvaluateService evaluateService;


    @Autowired
    ChatGPTServiceImpl chatGPTService;

    public EvaluateController(EvaluateService evaluateService) {
        this.evaluateService = evaluateService;
    }
    @PostMapping("/description/evaluates")
    public ResponseEntity<?> checkSentences(@RequestBody EvaluateDescriptionDto dto) {
        return ResponseEntity.ok(evaluateService.checkSentencesSecond(dto));
    }

    @GetMapping("{user-id}/cv/{cv-id}/evaluate")
    @PreAuthorize("hasAnyAuthority('update:candidate', 'update:expert', 'read:candidate')")
    public ScoreDto getOverviewEvaluateCv(@PathVariable("user-id") int userId, @PathVariable("cv-id") int cvId) throws JsonProcessingException {
        return evaluateService.getEvaluateCv(userId, cvId);
    }

}
