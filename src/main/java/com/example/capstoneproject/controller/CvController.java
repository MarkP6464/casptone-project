package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.CvViewDto;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.exception.ResourceNotFoundException;
import com.example.capstoneproject.service.CvService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import sun.security.provider.certpath.OCSPResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class CvController {
    @Autowired
    CvService cvService;

    public CvController(CvService cvService) {
        this.cvService = cvService;
    }

    @GetMapping("/{user-id}/cvs")
    public List<CvViewDto> getCvsById(@PathVariable("user-id") Integer UsersId, @RequestParam(required = false) String content) {
        return cvService.GetCvsById(UsersId, content);
    }

    @PostMapping("/user/{user-id}/cv/{cv-id}/duplicate")
    public CvDto duplicationCv(@PathVariable("user-id") Integer userId, @PathVariable("cv-id") Integer cvId) throws JsonProcessingException {
        return  cvService.duplicateCv(userId, cvId);
    }

    @GetMapping("/{user-id}/cv/{cv-id}")
    public CvAddNewDto getCvAndRelationsByCvId(@PathVariable("user-id") int UsersId, @PathVariable("cv-id") int cvId) throws JsonProcessingException {
        return cvService.GetCvByCvId(UsersId, cvId);
    }

    @PostMapping("/{user-id}/cv")
    public CvAddNewDto postCv(@PathVariable("user-id") int UsersId, @RequestBody CvBodyDto Dto) throws JsonProcessingException {
        return cvService.createCv(UsersId, Dto);
    }

    @PutMapping("/{user-id}/cv/{cv-id}/cv-body")
    public String updateCvBody(@PathVariable("user-id") int UsersId, @PathVariable("cv-id") int cvId, @RequestBody CvBodyDto Dto) throws JsonProcessingException {
        boolean check = cvService.updateCvBody(cvId, Dto);
        if (check) {
            return "Changes saved";
        } else {
            return "Changes fail";
        }
    }

    @GetMapping("/cv/{cv-id}/finish-up")
    public CvAddNewDto getFinishUp(@PathVariable("cv-id") int cvId) throws JsonProcessingException {
        return cvService.finishUp(cvId);
    }

    @PutMapping("/{user-id}/cv/{cv-id}/summary")
    public String updateSummary(@PathVariable("user-id") int UsersId, @PathVariable("cv-id") int cvId, @RequestBody CvUpdateSumDto Dto) {
        boolean check = cvService.updateCvSummary(UsersId, cvId, Dto);
        if (check) {
            return "Changes saved";
        } else {
            return "Changes fail";
        }
    }

    @PutMapping("/{user-id}/contact")
    public UsersViewDto updateContact(@PathVariable("user-id") int UsersId, @RequestBody UsersViewDto dto) {
        return cvService.updateCvContact(UsersId, dto);
    }

//    @PutMapping("/{user-id}/cv/{cv-id}/template/{template-id}")
//    public String updateTemplate(@PathVariable("user-id") int UsersId, @PathVariable("cv-id") int cvId, @PathVariable("template-id") int templateId) {
//        boolean check = cvService.updateCvTemplate(UsersId, cvId, templateId);
//        if (check) {
//            return "Changes saved";
//        } else {
//            return "Changes fail";
//        }
//    }

    @DeleteMapping("/{user-id}/cv/{cv-id}")
    public String deleteCv(@PathVariable("user-id") int UsersId, @PathVariable("cv-id") int cvId) {
        cvService.deleteCvById(UsersId, cvId);
        return "Delete successful";
    }

    @GetMapping("{user-id}/cv/{cv-id}/evaluate")
    public List<ScoreDto> getEvaluateCV(@PathVariable("user-id") int userId, @PathVariable("cv-id") int cvId) throws JsonProcessingException {
        return cvService.getEvaluateCv(userId, cvId);
    }

    @PutMapping("/{user-id}/cv/{cv-id}/public")
    public String searchable(@PathVariable("user-id") int userId, @PathVariable("cv-id") int cvId) {
        boolean check = cvService.searchable(userId, cvId);
        if (check) {
            return "Public saved";
        } else {
            return "Public fail";
        }
    }

    @GetMapping("/cvs/public")
    public List<CvAddNewDto> getSearchable(@RequestParam(required = false) String field) {
        return cvService.getListSearchable(field);
    }

    @GetMapping("/{user-id}/cvs/resume")
    public List<CvResumeDto> getListResume(@PathVariable("user-id") Integer userId) {
        return cvService.getListResume(userId);
    }

    @GetMapping("/{user-id}/cv/{cv-id}/experiences/role-names")
    public List<ExperienceRoleDto> getListExperienceRole(@PathVariable("user-id") Integer userId, @PathVariable("cv-id") Integer cvId) throws JsonProcessingException {
        return cvService.getListExperienceRole(userId,cvId);
    }



    @GetMapping("/{userId}/cv/{cvId}/micro")
    public Cv findByUserIdAndId(@PathVariable("userId") Integer userId, @PathVariable("cvId") Integer cvId) throws JsonProcessingException {
        return cvService.findByUser_IdAndId(userId, cvId);
    }
}
