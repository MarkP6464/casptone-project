package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.AtsDto;
import com.example.capstoneproject.Dto.ChatRequest;
import com.example.capstoneproject.Dto.ResultDto;
import com.example.capstoneproject.service.SentenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/evaluates")
public class EvaluateController {

    @Autowired
    SentenceService sentenceService;

    public EvaluateController(SentenceService sentenceService) {
        this.sentenceService = sentenceService;
    }
    @GetMapping("/{description}")
    public ResultDto checkSentences(@RequestParam String sentences) {
        return sentenceService.checkSentences(sentences);
    }

    @PostMapping("/extractKeywords")
    public List<AtsDto> extractKeywords(@RequestBody ChatRequest chatRequest) {
        List<AtsDto> keywordsList = sentenceService.ListAts(chatRequest);
        if (keywordsList.isEmpty()) {
            keywordsList = sentenceService.ListAts(chatRequest);
        }
        return keywordsList;
    }
}
