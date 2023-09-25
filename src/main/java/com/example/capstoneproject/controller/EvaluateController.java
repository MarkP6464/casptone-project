package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.ResultDto;
import com.example.capstoneproject.service.SentenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
