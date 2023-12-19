package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.responses.AdminConfigurationApiResponse;
import com.example.capstoneproject.Dto.responses.AdminConfigurationRatioResponse;
import com.example.capstoneproject.Dto.responses.AdminConfigurationResponse;
import com.example.capstoneproject.Dto.responses.AdminDateChartResponse;
import com.example.capstoneproject.enums.TypeChart;
import com.example.capstoneproject.service.AdminConfigurationService;
import com.example.capstoneproject.service.impl.ChatGPTServiceImpl;
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

    @Autowired
    ChatGPTServiceImpl chatGPTService;

    @GetMapping("/information/config")
    @PreAuthorize("hasAnyAuthority('read:admin', 'read:hr')")
    public ResponseEntity<?> getAdminConfigurationInfo() throws JsonProcessingException {
        AdminConfigurationResponse dto = adminConfigurationService.getByAdminId(1);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/information/config")
    @PreAuthorize("hasAnyAuthority('update:admin')")
    public ResponseEntity<?> update(@RequestBody AdminConfigurationRatioResponse dto) throws JsonProcessingException {
        return ResponseEntity.ok(adminConfigurationService.update(dto));
    }

    @PutMapping("/information/config/api-key")
    @PreAuthorize("hasAnyAuthority('update:admin')")
    public ResponseEntity<?> updateApi(@RequestBody AdminConfigurationApiResponse dto) throws JsonProcessingException {
        return ResponseEntity.ok(adminConfigurationService.updateApi(dto));
    }

    @PostMapping("/config/api-key/check")
    @PreAuthorize("hasAnyAuthority('update:admin')")
    public ResponseEntity<?> checkApiKey() {
        return ResponseEntity.ok(chatGPTService.isChatGptApiKeyValid());
    }

    @PostMapping("/admin/{admin-id}/dashboard/chart")
    @PreAuthorize("hasAnyAuthority('read:admin')")
    public ResponseEntity<?> getChart(@PathVariable("admin-id") Integer adminId, @RequestBody AdminDateChartResponse dto, @RequestParam(name = "chart", required = false) TypeChart chart) {
        if (chart == null) {
            chart = TypeChart.Account;
        }

        return ResponseEntity.ok(adminConfigurationService.getChart(adminId, dto, chart));
    }



}
