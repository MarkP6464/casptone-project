package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.CertificationDto;
import com.example.capstoneproject.Dto.CertificationViewDto;
import com.example.capstoneproject.service.CertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
public class CertificationController {

    @Autowired
    CertificationService certificationService;

    public CertificationController(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    @GetMapping("/{customerId}/certifications")
    public List<CertificationViewDto> getAllCertification(@PathVariable("customerId") int customerId) {
        return certificationService.getAllCertification(customerId);
    }

    @PostMapping("/{customerId}/certifications")
    public CertificationDto postCertification(@PathVariable("customerId") int customerId, @RequestBody CertificationDto Dto) {
        return certificationService.createCertification(customerId,Dto);
    }

    @PutMapping("/{customerId}/certifications/{certificationId}")
    public String updateCertification(@PathVariable("customerId") int customerId, @PathVariable("certificationId") int certificationId, @RequestBody CertificationDto Dto) {
        boolean check = certificationService.updateCertification(customerId,certificationId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{customerId}/certifications/{certificationId}")
    public String deleteCertification(@PathVariable("customerId") int customerId, @PathVariable("certificationId") int certificationId) {
        certificationService.deleteCertificationById(customerId,certificationId);
        return "Delete successful";
    }
}
