package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.AtsDto;
import com.example.capstoneproject.Dto.ChatRequest;
import com.example.capstoneproject.Dto.ResultDto;
import com.example.capstoneproject.service.EvaluateService;
import com.example.capstoneproject.service.impl.ChatGPTServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @GetMapping("/{description}")
    public ResultDto checkSentences(@RequestParam String sentences) {
        return evaluateService.checkSentences(sentences);
    }

    @PostMapping("/extractKeywords")
    public List<AtsDto> extractKeywords(@RequestBody ChatRequest chatRequest) {
        List<AtsDto> keywordsList = evaluateService.ListAts(chatRequest);
        if (keywordsList.isEmpty()) {
            keywordsList = evaluateService.ListAts(chatRequest);
        }
        return keywordsList;
    }
}
