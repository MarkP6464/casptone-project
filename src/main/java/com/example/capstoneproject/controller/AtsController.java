package com.example.capstoneproject.controller;

import com.example.capstoneproject.service.AtsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ats")
public class AtsController {

    @Autowired
    AtsService atsService;

    public AtsController(AtsService atsService) {
        this.atsService = atsService;
    }

    @DeleteMapping("/{ats-id}")
    @PreAuthorize("hasAuthority('delete:candidate')")
    public ResponseEntity<?> deleteAts(@PathVariable("ats-id") Integer atsId) {
        if (atsService.deleteAts(atsId)) {
            return ResponseEntity.ok("Delete success");
        } else {
            return ResponseEntity.badRequest().body("Delete failed");
        }
    }
}
