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
    @PostMapping("/description")
    public ResponseEntity<?> checkSentences(@RequestBody EvaluateDescriptionDto dto) {
        return ResponseEntity.ok(evaluateService.checkSentencesSecond(dto));
    }

}
