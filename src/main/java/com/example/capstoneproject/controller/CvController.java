package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.CvViewDto;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.service.CvService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasAuthority('read:candidate')")
    public List<CvViewDto> getCvsById(@PathVariable("user-id") Integer UsersId, @RequestParam(required = false) String content) {
        return cvService.GetCvsById(UsersId, content);
    }

    @PostMapping("/user/{user-id}/cv/{cv-id}/duplicate")
    @PreAuthorize("hasAuthority('create:candidate')")
    public CvDto duplicationCv(@PathVariable("user-id") Integer userId, @PathVariable("cv-id") Integer cvId) throws JsonProcessingException {
        return  cvService.duplicateCv(userId, cvId);
    }

    @GetMapping("/{user-id}/cv/{cv-id}")
    @PreAuthorize("hasAuthority('read:candidate')")
    public CvAddNewDto getCvAndRelationsByCvId(@PathVariable("user-id") int UsersId, @PathVariable("cv-id") int cvId) throws JsonProcessingException {
        return cvService.GetCvByCvId(UsersId, cvId);
    }

    @PostMapping("/{user-id}/cv")
    @PreAuthorize("hasAuthority('create:candidate')")
    public CvAddNewDto postCv(@PathVariable("user-id") int UsersId, @RequestBody CvBodyDto Dto) throws JsonProcessingException {
        return cvService.createCv(UsersId, Dto);
    }

    @PutMapping("/{user-id}/cv/{cv-id}/cv-body")
    @PreAuthorize("hasAuthority('update:candidate')")
    public String updateCvBody(@PathVariable("user-id") int UsersId, @PathVariable("cv-id") int cvId, @RequestBody CvBodyDto Dto) throws JsonProcessingException {
        boolean check = cvService.updateCvBody(cvId, Dto);
        if (check) {
            return "Changes saved";
        } else {
            return "Changes fail";
        }
    }

    @GetMapping("/cv/{cv-id}/finish-up")
    @PreAuthorize("hasAuthority('read:candidate')")
    public CvAddNewDto getFinishUp(@PathVariable("cv-id") int cvId) throws JsonProcessingException {
        return cvService.finishUp(cvId);
    }

    @PutMapping("/{user-id}/cv/{cv-id}/summary")
    @PreAuthorize("hasAuthority('update:candidate')")
    public String updateSummary(@PathVariable("user-id") int UsersId, @PathVariable("cv-id") int cvId, @RequestBody CvUpdateSumDto Dto) {
        boolean check = cvService.updateCvSummary(UsersId, cvId, Dto);
        if (check) {
            return "Changes saved";
        } else {
            return "Changes fail";
        }
    }

    @PutMapping("/{user-id}/contact")
    @PreAuthorize("hasAuthority('update:candidate')")
    public UsersViewDto updateContact(@PathVariable("user-id") int UsersId, @RequestBody UsersViewDto dto) {
        return cvService.updateCvContact(UsersId, dto);
    }

    @DeleteMapping("/{user-id}/cv/{cv-id}")
    @PreAuthorize("hasAuthority('delete:candidate')")
    public String deleteCv(@PathVariable("user-id") int UsersId, @PathVariable("cv-id") int cvId) {
        cvService.deleteCvById(UsersId, cvId);
        return "Delete successful";
    }

    @GetMapping("{user-id}/cv/{cv-id}/evaluate")
    @PreAuthorize("hasAuthority('read:candidate')")
    public List<ScoreDto> getEvaluateCV(@PathVariable("user-id") int userId, @PathVariable("cv-id") int cvId) throws JsonProcessingException {
        return cvService.getEvaluateCv(userId, cvId);
    }

    @PutMapping("/{user-id}/cv/{cv-id}/public")
    @PreAuthorize("hasAuthority('read:candidate')")
    public String searchable(@PathVariable("user-id") int userId, @PathVariable("cv-id") int cvId) {
        boolean check = cvService.searchable(userId, cvId);
        if (check) {
            return "Public saved";
        } else {
            return "Public fail";
        }
    }

    @GetMapping("/cvs/public")
    @PreAuthorize("hasAuthority('read:candidate')")
    public List<CvAddNewDto> getSearchable(@RequestParam(required = false) String field) {
        return cvService.getListSearchable(field);
    }

    @GetMapping("/{user-id}/cvs/resume")
    @PreAuthorize("hasAuthority('read:candidate')")
    public List<CvResumeDto> getListResume(@PathVariable("user-id") Integer userId) {
        return cvService.getListResume(userId);
    }

    @GetMapping("/{user-id}/cv/{cv-id}/experiences/role-names")
    @PreAuthorize("hasAuthority('read:candidate')")
    public List<ExperienceRoleDto> getListExperienceRole(@PathVariable("user-id") Integer userId, @PathVariable("cv-id") Integer cvId) throws JsonProcessingException {
        return cvService.getListExperienceRole(userId,cvId);
    }


    @GetMapping("/{userId}/cv/{cvId}/micro")
    @PreAuthorize("hasAuthority('read:candidate')")
    public Cv findByUserIdAndId(@PathVariable("userId") Integer userId, @PathVariable("cvId") Integer cvId) throws JsonProcessingException {
        return cvService.findByUser_IdAndId(userId, cvId);
    }
}
