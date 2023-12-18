package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.HRDto;
import com.example.capstoneproject.Dto.responses.HRResponse;
import com.example.capstoneproject.Dto.responses.TransactionResponse;
import com.example.capstoneproject.service.HRService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/hr")
public class HRController {

    @Autowired
    HRService hrService;

    @GetMapping("/{hr-id}/information/config")
    @PreAuthorize("hasAnyAuthority('read:hr')")
    public ResponseEntity<?> getHRInfo(@PathVariable("hr-id") Integer hrId) throws JsonProcessingException {
        HRDto dto = hrService.get(hrId);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{hr-id}/information/config")
    @PreAuthorize("hasAnyAuthority('update:hr')")
    public ResponseEntity<?> update(@PathVariable("hr-id") Integer hrId, @RequestBody HRResponse dto) throws JsonProcessingException {
        dto.setId(hrId);
        return ResponseEntity.ok(hrService.update(dto));
    }

    @PutMapping("/{hr-id}/register-vip")
    @PreAuthorize("hasAnyAuthority('update:hr')")
    public ResponseEntity<?> registerVip(@PathVariable("hr-id") Integer hrId, @RequestBody TransactionResponse transactionDto) throws Exception {
        transactionDto.setUserId(hrId);
        return ResponseEntity.ok(hrService.register(transactionDto));
    }
}
