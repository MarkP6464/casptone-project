package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.AdminConfigurationDto;
import com.example.capstoneproject.Dto.responses.AdminConfigurationResponse;
import com.example.capstoneproject.Dto.responses.TransactionResponse;
import com.example.capstoneproject.entity.AdminConfiguration;
import com.example.capstoneproject.service.AdminConfigurationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminConfigurationController {

    @Autowired
    AdminConfigurationService adminConfigurationService;

    @GetMapping("/information/config")
    @PreAuthorize("hasAnyAuthority('read:admin-messages')")
    public ResponseEntity<?> getAdminConfigurationInfo() {
        AdminConfigurationResponse dto = adminConfigurationService.getByAdminId(1);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/information/config")
    @PreAuthorize("hasAnyAuthority('update:admin-messages')")
    public ResponseEntity<?> update(@RequestBody AdminConfigurationResponse dto) {
        return ResponseEntity.ok(adminConfigurationService.update(dto));
    }

}
