package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.AtsDto;
import com.example.capstoneproject.Dto.JobDescriptionDto;
import com.example.capstoneproject.Dto.JobDescriptionViewDto;
import com.example.capstoneproject.service.EvaluateService;
import com.example.capstoneproject.service.impl.ChatGPTServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/evaluates")
public class EvaluateController {

    @Autowired
    EvaluateService evaluateService;

    @Autowired
    ChatGPTServiceImpl chatGPTService;

    public EvaluateController(EvaluateService evaluateService) {
        this.evaluateService = evaluateService;
    }
//    @GetMapping("/{description}")
//    public ResultDto checkSentences(@RequestParam String sentences) {
//        return evaluateService.checkSentences(sentences);
//    }

    @PostMapping("{cvId}/extractKeywords")
    public List<AtsDto> extractKeywords(@PathVariable("cvId") int cvId, @PathVariable("jobId") int jobId,  @RequestBody JobDescriptionDto jobDescriptionViewDto) throws JsonProcessingException {
        List<AtsDto> keywordsList = evaluateService.ListAts(cvId,jobId, jobDescriptionViewDto);
        if (keywordsList.isEmpty()) {
            keywordsList = evaluateService.ListAts(cvId,jobId, jobDescriptionViewDto);
        }
        return keywordsList;
    }
}
