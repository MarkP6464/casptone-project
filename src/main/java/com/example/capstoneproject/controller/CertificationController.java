package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.CertificationDto;
import com.example.capstoneproject.service.CertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cv/certifications")
public class CertificationController {

    @Autowired
    CertificationService certificationService;

    public CertificationController(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    @GetMapping
    public List<CertificationDto> getAllCertification() {
        return certificationService.getAll();
    }

    @PostMapping
    public CertificationDto postCertification(@RequestBody CertificationDto Dto) {
        return certificationService.create(Dto);
    }

    @PutMapping("/{certificationId}")
    public CertificationDto updateCertification(@PathVariable("certificationId") int certificationId, @RequestBody CertificationDto Dto) {
        return certificationService.update(certificationId, Dto);
    }

    @DeleteMapping("/{certificationId}")
    public String deleteCertification(@PathVariable("certificationId") int certificationId) {
        certificationService.deleteById(certificationId);
        return "Delete successful";
    }
}
