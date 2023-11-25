package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.HRDto;
import com.example.capstoneproject.exception.ResourceNotFoundException;
import com.example.capstoneproject.service.HRService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/hr")
public class HRController {

    @Autowired
    HRService hrService;

    @GetMapping("/{hr-id}")
    @PreAuthorize("hasAnyAuthority('read:hr')")
    public ResponseEntity<?> getHRInfo(@PathVariable("hr-id") Integer hrId) throws JsonProcessingException {
        HRDto dto = hrService.get(hrId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAnyAuthority('create:hr')")
    public ResponseEntity<?> update(@RequestBody HRDto dto) throws JsonProcessingException {
        return ResponseEntity.ok(hrService.update(dto));
    }

    @GetMapping("/register-vip")
    @PreAuthorize("hasAnyAuthority('create:hr')")
    public ResponseEntity<?> registerVip() throws Exception {
        return ResponseEntity.ok(hrService.register());
    }
}
