package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.HistorySummaryDto;
import com.example.capstoneproject.Dto.responses.HistorySummaryViewDto;
import com.example.capstoneproject.service.HistorySummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class HistorySummaryController {

    @Autowired
    private HistorySummaryService historySummaryService;

    @GetMapping("/user/cv/{cv-id}/summary/history-summaries")
    public ResponseEntity<List<HistorySummaryDto>> getHistorySummaries(@PathVariable("cv-id") Integer cvId) {
        List<HistorySummaryDto> historySummaries = historySummaryService.getHistorySummaries(cvId);
        return new ResponseEntity<>(historySummaries, HttpStatus.OK);
    }

    @GetMapping("/user/cv/summary/history-summary/{history-summary-id}")
    public ResponseEntity<HistorySummaryViewDto> getHistorySummary(@PathVariable("history-summary-id") Integer summaryId) {
        HistorySummaryViewDto historySummary = historySummaryService.getHistorySummary(summaryId);
        if (historySummary != null) {
            return new ResponseEntity<>(historySummary, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

