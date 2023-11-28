package com.example.capstoneproject.controller;

import com.example.capstoneproject.exception.ResourceNotFoundException;
import com.example.capstoneproject.service.HistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
public class HistoryController {

    @Autowired
    HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/user/cv/history/{history-id}")
    @PreAuthorize("hasAnyAuthority('read:candidate','read:expert','read:hr')")
    public ResponseEntity<?> getHistory(@PathVariable("history-id") Integer historyId) throws JsonProcessingException {
        if(Objects.nonNull(historyService.getHistory(historyId))){
            return ResponseEntity.ok(historyService.getHistory(historyId));
        }else{
            throw new ResourceNotFoundException("");
        }
    }

    @GetMapping("/user/{user-id}/cv/{cv-id}/histories")
    @PreAuthorize("hasAnyAuthority('read:candidate','read:expert')")
    public ResponseEntity<?> getListHistory(@PathVariable("user-id") Integer userId, @PathVariable("cv-id") Integer cvId) {
        return ResponseEntity.ok(historyService.getListHistoryDate(userId,cvId));
    }

    @PostMapping("/user/{user-id}/cv/{cv-id}/history")
    @PreAuthorize("hasAnyAuthority('create:candidate','create:expert')")
    public ResponseEntity<?> createHistory(@PathVariable("user-id") Integer userId, @PathVariable("cv-id") Integer cvId) throws JsonProcessingException {
        return ResponseEntity.ok(historyService.create(userId, cvId));
    }
}
