package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.ExpertUpdateDto;
import com.example.capstoneproject.Dto.TransactionDto;
import com.example.capstoneproject.Dto.request.HRBankRequest;
import com.example.capstoneproject.Dto.responses.ExpertConfigViewDto;
import com.example.capstoneproject.Dto.responses.ExpertViewChooseDto;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.service.ExpertService;
import com.example.capstoneproject.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ExpertController {
    @Autowired
    TransactionService transactionService;

    @Autowired
    ExpertService expertService;

    public ExpertController(ExpertService expertService) {
        this.expertService = expertService;
    }

    @PutMapping("/expert/{expert-id}/cv/information/config")
    @PreAuthorize("hasAuthority('update:expert')")
    public ResponseEntity<?> updateExpert(@PathVariable("expert-id") Integer expertId, @RequestBody ExpertUpdateDto dto) {
        if (expertService.updateExpert(expertId, dto)) {
            return ResponseEntity.ok("Update success");
        } else {
            return ResponseEntity.badRequest().body("Update failed");
        }
    }

    @GetMapping("/expert/{expert-id}")
    @PreAuthorize("hasAuthority('read:candidate')")
    public ResponseEntity<?> getExpert(@PathVariable("expert-id") Integer expertId){
        return ResponseEntity.ok(expertService.getDetailExpert(expertId));
    }

    @GetMapping("/experts")
    @PreAuthorize("hasAuthority('read:candidate')")
    public ResponseEntity<?> getAllExpert(@RequestParam(required = false) String search) {
        try {
            List<ExpertViewChooseDto> expertList = expertService.getExpertList(search);
            return ResponseEntity.ok(expertList);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/expert/{expert-id}/information/config")
    @PreAuthorize("hasAuthority('read:expert')")
    public ResponseEntity<ExpertConfigViewDto> getExpertConfig(@PathVariable("expert-id") Integer expertId) {
        return new ResponseEntity<>(expertService.getExpertConfig(expertId), HttpStatus.OK);
    }

    @PutMapping("/{expert-id}/information/config-bank")
    @PreAuthorize("hasAnyAuthority('update:expert')")
    public ResponseEntity<?> update(@PathVariable("expert-id") Integer hrId, @RequestBody HRBankRequest dto) throws JsonProcessingException {
        dto.setId(hrId);
        return ResponseEntity.ok(expertService.update(dto));
    }

    @GetMapping("expert/get-all/{user-id}")
    @PreAuthorize("hasAnyAuthority( 'read:expert', 'read:admin')")
    public List<TransactionDto> getTypeWithdraw(@PathVariable("user-id") String sentId){
        List<TransactionDto> list = transactionService.getAllTransactionType(sentId);
        return list;
    }

}
