package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.CertificationDto;
import com.example.capstoneproject.Dto.responses.CertificationViewDto;
import com.example.capstoneproject.service.CertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/Users")
public class CertificationController {

    @Autowired
    CertificationService certificationService;

    public CertificationController(CertificationService certificationService) {
        this.certificationService = certificationService;
    }

    @GetMapping("/{UsersId}/certifications")
    public List<CertificationViewDto> getAllCertification(@PathVariable("UsersId") int UsersId) {
        return certificationService.getAllCertification(UsersId);
    }

    @PostMapping("/{UsersId}/certifications")
    public CertificationDto postCertification(@PathVariable("UsersId") int UsersId, @RequestBody CertificationDto Dto) {
        return certificationService.createCertification(UsersId,Dto);
    }

    @PutMapping("/{UsersId}/certifications/{certificationId}")
    public String updateCertification(@PathVariable("UsersId") int UsersId, @PathVariable("certificationId") int certificationId, @RequestBody CertificationDto Dto) {
        boolean check = certificationService.updateCertification(UsersId,certificationId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{UsersId}/certifications/{certificationId}")
    public String deleteCertification(@PathVariable("UsersId") int UsersId, @PathVariable("certificationId") int certificationId) {
        certificationService.deleteCertificationById(UsersId,certificationId);
        return "Delete successful";
    }
}
