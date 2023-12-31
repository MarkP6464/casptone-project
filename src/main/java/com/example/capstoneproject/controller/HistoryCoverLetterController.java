package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.HistoryCoverLetterDto;
import com.example.capstoneproject.Dto.HistorySummaryDto;
import com.example.capstoneproject.Dto.responses.HistorySummaryViewDto;
import com.example.capstoneproject.entity.HistoryCoverLetter;
import com.example.capstoneproject.service.HistoryCoverLetterService;
import com.example.capstoneproject.service.HistorySummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class HistoryCoverLetterController {

    @Autowired
    private HistoryCoverLetterService historyCoverLetterService;

    @GetMapping("/user/cv/{letter-id}/history-cover-letter")
    @PreAuthorize("hasAnyAuthority('read:candidate','read:expert')")
    public ResponseEntity<HistoryCoverLetterDto> getHistoryCoverLetters(@PathVariable("letter-id") Integer letterId) {
        HistoryCoverLetterDto historyCoverLetter = historyCoverLetterService.get(letterId);
        return ResponseEntity.status(HttpStatus.OK).body(historyCoverLetter);
    }
}

