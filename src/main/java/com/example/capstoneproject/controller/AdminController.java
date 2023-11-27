package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.AdminConfigurationDto;
import com.example.capstoneproject.Dto.HRDto;
import com.example.capstoneproject.Dto.responses.AdminConfigurationResponse;
import com.example.capstoneproject.Dto.responses.HRResponse;
import com.example.capstoneproject.Dto.responses.TransactionResponse;
import com.example.capstoneproject.service.AdminConfigurationService;
import com.example.capstoneproject.service.HRService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    AdminConfigurationService adminConfigurationService;

    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority('read:admin')")
    public ResponseEntity<?> getHRInfo() throws JsonProcessingException {
        AdminConfigurationResponse dto = adminConfigurationService.getByAdminId(1);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/information/config")
    @PreAuthorize("hasAnyAuthority('update:admin')")
    public ResponseEntity<?> update(@RequestBody AdminConfigurationResponse dto) throws JsonProcessingException {
        return ResponseEntity.ok(adminConfigurationService.update(dto));
    }
}
