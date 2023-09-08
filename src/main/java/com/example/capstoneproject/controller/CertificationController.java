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

    @PostMapping("/{cvId}/certifications")
    public CertificationDto postCertification(@PathVariable("cvId") int cvId, @RequestBody CertificationDto Dto) {
        return certificationService.createCertification(cvId,Dto);
    }

    @PutMapping("/{cvId}/certifications/{certificationId}")
    public String updateCertification(@PathVariable("cvId") int cvId, @PathVariable("certificationId") int certificationId, @RequestBody CertificationDto Dto) {
        boolean check = certificationService.updateCertification(cvId,certificationId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{cvId}/certifications/{certificationId}")
    public String deleteCertification(@PathVariable("cvId") int cvId, @PathVariable("certificationId") int certificationId) {
        certificationService.deleteCertificationById(cvId,certificationId);
        return "Delete successful";
    }
}
