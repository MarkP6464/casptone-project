package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.EvaluateCriteriaDto;
import com.example.capstoneproject.Dto.EvaluateDescriptionDto;
import com.example.capstoneproject.Dto.EvaluateScoreDto;
import com.example.capstoneproject.Dto.ScoreDto;
import com.example.capstoneproject.enums.SortOrder;
import com.example.capstoneproject.service.CvService;
import com.example.capstoneproject.service.EvaluateService;
import com.example.capstoneproject.service.impl.ChatGPTServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class EvaluateController {

    @Autowired
    EvaluateService evaluateService;


    @Autowired
    ChatGPTServiceImpl chatGPTService;

    public EvaluateController(EvaluateService evaluateService) {
        this.evaluateService = evaluateService;
    }
    @PostMapping("/user/description/evaluates")
    public ResponseEntity<?> checkSentences(@RequestBody EvaluateDescriptionDto dto) {
        return ResponseEntity.ok(evaluateService.checkSentencesSecond(dto));
    }

    @GetMapping("/user/{user-id}/cv/{cv-id}/evaluate")
    @PreAuthorize("hasAnyAuthority('update:candidate', 'update:expert', 'read:candidate')")
    public ScoreDto getOverviewEvaluateCv(@PathVariable("user-id") int userId, @PathVariable("cv-id") int cvId) throws JsonProcessingException {
        return evaluateService.getEvaluateCv(userId, cvId);
    }

    @PutMapping("/admin/{admin-id}/evaluate/{evaluate-id}/score")
    @PreAuthorize("hasAuthority('update:admin')")
    public ResponseEntity<?> updateScore(@PathVariable("admin-id") Integer adminId, @PathVariable("evaluate-id") Integer evaluateId, @RequestBody EvaluateScoreDto dto) {
        return ResponseEntity.ok(evaluateService.updateScoreEvaluate(adminId,evaluateId,dto));
    }

    @PutMapping("/admin/{admin-id}/evaluate/{evaluate-id}/criteria")
    @PreAuthorize("hasAuthority('update:admin')")
    public ResponseEntity<?> updateCriteria(@PathVariable("admin-id") Integer adminId, @PathVariable("evaluate-id") Integer evaluateId, @RequestBody EvaluateCriteriaDto dto) {
        return ResponseEntity.ok(evaluateService.updateCriteriaEvaluate(adminId,evaluateId,dto));
    }

    @PutMapping("/admin/{admin-id}/evaluate/{evaluate-id}/score-criteria")
    @PreAuthorize("hasAuthority('update:admin')")
    public ResponseEntity<?> updateScoreAndCriteria(@PathVariable("admin-id") Integer adminId, @PathVariable("evaluate-id") Integer evaluateId, @RequestBody EvaluateScoreDto dto) {
        return ResponseEntity.ok(evaluateService.updateScoreAndCriteriaEvaluate(adminId,evaluateId,dto));
    }

    @GetMapping("/admin/{admin-id}/evaluates/config")
    @PreAuthorize("hasAuthority('read:admin')")
    public ResponseEntity<?> getEvaluateConfig(
            @PathVariable("admin-id") Integer adminId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) SortOrder sort
    ) {
        return ResponseEntity.ok(evaluateService.viewEvaluate(adminId, search, sort));
    }


}
