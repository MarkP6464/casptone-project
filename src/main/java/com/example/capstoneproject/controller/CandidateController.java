package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.CandidateDto;
import com.example.capstoneproject.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/candidate")
public class CandidateController {

    @Autowired
    CandidateService candidateService;

    @PutMapping("/{candidate-id}/information/config")
    @PreAuthorize("hasAuthority('update:candidate')")
    public ResponseEntity<?> updateCandidate(@PathVariable("candidate-id") Integer candidateId, @RequestBody CandidateDto dto) {
        boolean updateResult = candidateService.updateCandidate(candidateId, dto);
        if (updateResult) {
            return new ResponseEntity<>("Update successful", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Update fail", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{candidate-id}/information/config")
    @PreAuthorize("hasAuthority('read:candidate')")
    public ResponseEntity<CandidateDto> getCandidateConfig(@PathVariable("candidate-id") Integer candidateId) {
        CandidateDto candidateDto = candidateService.getCandidateConfig(candidateId);
        if (candidateDto != null) {
            return new ResponseEntity<>(candidateDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
