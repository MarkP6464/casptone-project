package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.CandidateDto;
import com.example.capstoneproject.Dto.CvAddNewDto;
import com.example.capstoneproject.Dto.responses.CandidateOverViewDto;
import com.example.capstoneproject.Dto.responses.CandidateViewDto;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.service.CandidateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CandidateController {

    @Autowired
    CandidateService candidateService;

    @PutMapping("/candidate/{candidate-id}/information/config")
    @PreAuthorize("hasAuthority('update:candidate')")
    public ResponseEntity<?> updateCandidate(@PathVariable("candidate-id") Integer candidateId, @RequestBody CandidateDto dto) {
        boolean updateResult = candidateService.updateCandidate(candidateId, dto);
        if (updateResult) {
            return new ResponseEntity<>("Update successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Update fail", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/candidate/{candidate-id}/information/config")
    @PreAuthorize("hasAuthority('read:candidate')")
    public ResponseEntity<CandidateViewDto> getCandidateConfig(@PathVariable("candidate-id") Integer candidateId) {
        CandidateViewDto candidateDto = candidateService.getCandidateConfig(candidateId);
        if (candidateDto != null) {
            return new ResponseEntity<>(candidateDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/candidate/{candidate-id}/information/cvs/publish")
    @PreAuthorize("hasAuthority('read:candidate')")
    public ResponseEntity<?> getAllCvPublishCandidate(@PathVariable("candidate-id") Integer candidateId) throws JsonProcessingException {
        List<CvAddNewDto> candidateDto = candidateService.getAllCvPublishCandidate(candidateId);
        if (!candidateDto.isEmpty()) {
            return new ResponseEntity<>(candidateDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/candidates/publish/information")
    @PreAuthorize("hasAuthority('read:hr')")
    public ResponseEntity<List<CandidateOverViewDto>> getAllCandidatePublish(@RequestParam(required = false) String search) {
        List<CandidateOverViewDto> candidates = candidateService.getAllCandidatePublish(search);
        return ResponseEntity.ok(candidates);
    }
}
