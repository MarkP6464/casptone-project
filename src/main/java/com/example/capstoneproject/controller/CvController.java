package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.service.CvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CvController {
    @Autowired
    CvService cvService;

    public CvController(CvService cvService) {
        this.cvService = cvService;
    }

    @GetMapping("/{customerId}/cvs")
    public List<CvDto> getCvsById(@PathVariable("customerId") int customerId) {
        return cvService.GetCvsById(customerId);
    }
    @GetMapping("/{customerId}/cv/{cvId}")
    public CvDto getCvsByCvId(@PathVariable("customerId") int customerId, @PathVariable("cvId") int cvId) {
        return cvService.GetCvsByCvId(customerId, cvId);
    }
    @PostMapping("/{customerId}/cv")
    public CvAddNewDto postCv(@PathVariable("customerId") int customerId, @RequestBody CvAddNewDto Dto) {
        return cvService.createCv(customerId,Dto);
    }
    @PutMapping("/{customerId}/cv/{cvId}/summary")
    public String updateSummary(@PathVariable("customerId") int customerId, @PathVariable("cvId") int cvId, @RequestBody CvUpdateSumDto Dto) {
        boolean check = cvService.updateCvSummary(customerId,cvId, Dto);
        if (check) {
            return "Changes saved";
        } else {
            return "Changes fail";
        }
    }
    @PutMapping("/{customerId}/cv/{cvId}/content")
    public String updateContent(@PathVariable("customerId") int customerId, @PathVariable("cvId") int cvId, @RequestBody CvAddNewDto Dto) {
        boolean check = cvService.updateCvContent(customerId,cvId, Dto);
        if (check) {
            return "Changes saved";
        } else {
            return "Changes fail";
        }
    }

    @PutMapping("/{customerId}/cv/{cvId}/contact/{contactId}")
    public String updateContact(@PathVariable("customerId") int customerId, @PathVariable("cvId") int cvId, @PathVariable("contactId") int contactId) {
        boolean check = cvService.updateCvContact(customerId,cvId, contactId);
        if (check) {
            return "Changes saved";
        } else {
            return "Changes fail";
        }
    }

    @PutMapping("/{customerId}/cv/{cvId}/template/{templateId}")
    public String updateTemplate(@PathVariable("customerId") int customerId, @PathVariable("cvId") int cvId, @PathVariable("templateId") int templateId) {
        boolean check = cvService.updateCvTemplate(customerId,cvId, templateId);
        if (check) {
            return "Changes saved";
        } else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{customerId}/cv/{cvId}")
    public String deleteCv(@PathVariable("customerId") int customerId, @PathVariable("cvId") int cvId) {
        cvService.deleteCvById(customerId, cvId);
        return "Delete successful";
    }
}
