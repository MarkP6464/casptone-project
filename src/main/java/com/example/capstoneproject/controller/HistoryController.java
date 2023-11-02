package com.example.capstoneproject.controller;

import com.example.capstoneproject.exception.ResourceNotFoundException;
import com.example.capstoneproject.service.HistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1")
public class HistoryController {

    @Autowired
    HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/user/{user-id}/cv/{cv-id}/history/{history-id}")
    public ResponseEntity<?> getHistory(@PathVariable("user-id") Integer userId, @PathVariable("cv-id") Integer cvId, @PathVariable("history-id") Integer historyId) throws JsonProcessingException {
        if(Objects.nonNull(historyService.getHistory(userId,cvId,historyId))){
            return ResponseEntity.ok(historyService.getHistory(userId,cvId,historyId));
        }else{
            throw new ResourceNotFoundException("kop");
        }
    }

    @GetMapping("/user/{user-id}/cv/{cv-id}/histories")
    public ResponseEntity<?> getListHistory(@PathVariable("user-id") Integer userId, @PathVariable("cv-id") Integer cvId) {
        return ResponseEntity.ok(historyService.getListHistoryDate(userId,cvId));
    }
}
