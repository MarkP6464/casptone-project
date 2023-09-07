package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.CertificationDto;
import com.example.capstoneproject.Dto.CertificationViewDto;
import com.example.capstoneproject.service.CertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cv")
public class CertificationController {

    @Autowired
    CertificationService certificationService;

    public CertificationController(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    @GetMapping("/{cvId}/certifications")
    public List<CertificationViewDto> getAllCertification(@PathVariable("cvId") int cvId) {
        return certificationService.getAllCertification(cvId);
    }

    @PostMapping("/certifications")
    public CertificationDto postCertification(@RequestBody CertificationDto Dto) {
        return certificationService.create(Dto);
    }

    @PutMapping("/certifications/{certificationId}")
    public String updateCertification(@PathVariable("certificationId") int certificationId, @RequestBody CertificationViewDto Dto) {
        boolean check = certificationService.updateCertification(certificationId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/certifications/{certificationId}")
    public String deleteCertification(@PathVariable("certificationId") int certificationId) {
        certificationService.deleteById(certificationId);
        return "Delete successful";
    }
}
