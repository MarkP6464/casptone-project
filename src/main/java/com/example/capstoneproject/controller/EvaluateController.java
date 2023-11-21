package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.EvaluateDescriptionDto;
import com.example.capstoneproject.service.EvaluateService;
import com.example.capstoneproject.service.impl.ChatGPTServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/description/evaluates")
public class EvaluateController {

    @Autowired
    EvaluateService evaluateService;

    @Autowired
    ChatGPTServiceImpl chatGPTService;

    public EvaluateController(EvaluateService evaluateService) {
        this.evaluateService = evaluateService;
    }
    @GetMapping()
    public ResponseEntity<?> checkSentences(EvaluateDescriptionDto dto) {
        return ResponseEntity.ok(evaluateService.checkSentencesSecond(dto));
    }

//    @PostMapping("{cvId}/extractKeywords")
//    public List<AtsDto> extractKeywords(@PathVariable("cvId") int cvId, @PathVariable("jobId") int jobId,  @RequestBody JobDescriptionDto jobDescriptionViewDto) throws JsonProcessingException {
//        List<AtsDto> keywordsList = evaluateService.ListAts(cvId,jobId, jobDescriptionViewDto);
//        if (keywordsList.isEmpty()) {
//            keywordsList = evaluateService.ListAts(cvId,jobId, jobDescriptionViewDto);
//        }
//        return keywordsList;
//    }
}
