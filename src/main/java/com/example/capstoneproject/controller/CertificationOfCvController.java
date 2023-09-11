package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.CertificationViewDto;
import com.example.capstoneproject.service.CertificationOfCvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cv")
public class CertificationOfCvController {

    @Autowired
    CertificationOfCvService certificationOfCvService;

    public CertificationOfCvController(CertificationOfCvService certificationOfCvService) {
        this.certificationOfCvService = certificationOfCvService;
    }

    @GetMapping("/{cvId}/certifications/certification-of-cv")
    public List<CertificationViewDto> getAllCertification(@PathVariable("cvId") int cvId) {
        return certificationOfCvService.getActiveCertificationsByCvId(cvId);
    }
    @GetMapping("/{cvId}/certifications")
    public List<CertificationViewDto> getAllCertificationNotYet(@PathVariable("cvId") int cvId) {
        return certificationOfCvService.getAllCertification(cvId);
    }
    @PostMapping("/{cvId}/certifications/{certificationId}")
    public String postCertificationOfCv(@PathVariable("cvId") int cvId, @PathVariable("certificationId") int certificationId) {
        boolean check = certificationOfCvService.createCertificationOfCv(cvId,certificationId);
        if(check){
            return "Addition success";
        }else {
            return "Addition fail";
        }
    }

    @DeleteMapping("/{cvId}/certifications/{certificationId}")
    public String deleteCertificationOfCv(@PathVariable("cvId") int cvId, @PathVariable("certificationId") int certificationId) {
        boolean check = certificationOfCvService.deleteCertificationOfCv(cvId,certificationId);
        if(check){
            return "Delete success";
        }else {
            return "Delete fail";
        }
    }
}
