package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.service.CvService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/Users")
public class CvController {
    @Autowired
    CvService cvService;

    public CvController(CvService cvService) {
        this.cvService = cvService;
    }

    @GetMapping("/{UsersId}/cvs")
    public List<CvDto> getCvsById(@PathVariable("UsersId") int UsersId) {
        return cvService.GetCvsById(UsersId);
    }
    @GetMapping("/{UsersId}/cv/{cvId}")
    public CvDto getCvsByCvId(@PathVariable("UsersId") int UsersId, @PathVariable("cvId") int cvId) throws JsonProcessingException {
        return cvService.GetCvsByCvId(UsersId, cvId);
    }
    @PostMapping("/{UsersId}/cv")
    public CvAddNewDto postCv(@PathVariable("UsersId") int UsersId, @RequestBody CvAddNewDto Dto) {
        return cvService.createCv(UsersId,Dto);
    }
    @PutMapping("/{UsersId}/updateCvBody/{cvId}")
    public String updateCvBody(@PathVariable("UsersId") int UsersId, @PathVariable("cvId") int cvId, @RequestBody CvBodyDto Dto) throws JsonProcessingException {
        boolean check = cvService.updateCvBody(UsersId,cvId, Dto);
        if (check) {
            return "Changes saved";
        } else {
            return "Changes fail";
        }
    }

    @GetMapping("/{UsersId}/getCvBody/{cvId}")
    public CvBodyDto getCvBody(@PathVariable("UsersId") int UsersId, @PathVariable("cvId") int cvId) throws JsonProcessingException {
        CvBodyDto cvBody = cvService.getCvBody(UsersId,cvId);
        return cvBody;
    }

    @PutMapping("/{UsersId}/cv/{cvId}/summary")
    public String updateSummary(@PathVariable("UsersId") int UsersId, @PathVariable("cvId") int cvId, @RequestBody CvUpdateSumDto Dto) {
        boolean check = cvService.updateCvSummary(UsersId,cvId, Dto);
        if (check) {
            return "Changes saved";
        } else {
            return "Changes fail";
        }
    }
    @PutMapping("/{UsersId}/cv/{cvId}/content")
    public String updateContent(@PathVariable("UsersId") int UsersId, @PathVariable("cvId") int cvId, @RequestBody CvAddNewDto Dto) {
        boolean check = cvService.updateCvContent(UsersId,cvId, Dto);
        if (check) {
            return "Changes saved";
        } else {
            return "Changes fail";
        }
    }

    @PutMapping("/{UsersId}/cv/{cvId}/contact/{contactId}")
    public String updateContact(@PathVariable("UsersId") int UsersId, @PathVariable("cvId") int cvId, @PathVariable("contactId") int contactId) {
        boolean check = cvService.updateCvContact(UsersId,cvId, contactId);
        if (check) {
            return "Changes saved";
        } else {
            return "Changes fail";
        }
    }

    @PutMapping("/{UsersId}/cv/{cvId}/template/{templateId}")
    public String updateTemplate(@PathVariable("UsersId") int UsersId, @PathVariable("cvId") int cvId, @PathVariable("templateId") int templateId) {
        boolean check = cvService.updateCvTemplate(UsersId,cvId, templateId);
        if (check) {
            return "Changes saved";
        } else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{UsersId}/cv/{cvId}")
    public String deleteCv(@PathVariable("UsersId") int UsersId, @PathVariable("cvId") int cvId) {
        cvService.deleteCvById(UsersId, cvId);
        return "Delete successful";
    }


}
