package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.CvViewDto;
import com.example.capstoneproject.Dto.responses.UsersCvViewDto;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.exception.InternalServerException;
import com.example.capstoneproject.service.CvService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    @PreAuthorize("hasAnyAuthority('read:candidate', 'read:expert')")
    public List<CvViewDto> getCvsById(@PathVariable("user-id") Integer UsersId, @RequestParam(required = false) String content) {
        return cvService.GetCvsById(UsersId, content);
    }

    @PostMapping("/user/{user-id}/cv/{cv-id}/duplicate")
    @PreAuthorize("hasAnyAuthority('create:candidate', 'create:expert')")
    public CvDto duplicationCv(@PathVariable("user-id") Integer userId, @PathVariable("cv-id") Integer cvId) throws JsonProcessingException {
        return  cvService.duplicateCv(userId, cvId);
    }

    @GetMapping("/{user-id}/cv/{cv-id}")
    @PreAuthorize("hasAnyAuthority('read:candidate', 'read:expert')")
    public CvAddNewDto getCvAndRelationsByCvId(@PathVariable("user-id") int UsersId, @PathVariable("cv-id") int cvId) throws JsonProcessingException {
        return cvService.GetCvByCvId(UsersId, cvId);
    }

    @PostMapping("/{user-id}/cv")
    @PreAuthorize("hasAnyAuthority('create:candidate', 'create:expert')")
    public CvAddNewDto createCv(@PathVariable("user-id") int UsersId, @RequestBody CvBodyDto Dto) throws JsonProcessingException {
        return cvService.createCv(UsersId, Dto);
    }

    @PutMapping("/{user-id}/cv/{cv-id}/cv-body")
    @PreAuthorize("hasAnyAuthority('update:candidate', 'update:expert')")
    public String updateCvBody(@PathVariable("user-id") int UsersId, @PathVariable("cv-id") int cvId, @RequestBody CvBodyDto Dto) throws JsonProcessingException {
        boolean check = cvService.updateCvBody(cvId, Dto);
        if (check) {
            return "Changes saved";
        } else {
            return "Changes fail";
        }
    }

    @GetMapping("/cv/{cv-id}/finish-up")
    @PreAuthorize("hasAnyAuthority('update:candidate', 'update:expert')")
    public CvAddNewDto getFinishUp(@PathVariable("cv-id") int cvId) throws JsonProcessingException {
        return cvService.finishUp(cvId);
    }

    @PutMapping("/{user-id}/cv/{cv-id}/summary")
    @PreAuthorize("hasAnyAuthority('update:candidate', 'update:expert')")
    public String updateSummary(@PathVariable("user-id") int UsersId, @PathVariable("cv-id") int cvId, @RequestBody CvUpdateSumDto Dto) {
        boolean check = cvService.updateCvSummary(UsersId, cvId, Dto);
        if (check) {
            return "Changes saved";
        } else {
            return "Changes fail";
        }
    }

    @PutMapping("/{user-id}/cv/{cv-id}/contact")
    @PreAuthorize("hasAnyAuthority('update:candidate', 'update:expert')")
    public ResponseEntity<?> updateContact1(@PathVariable("user-id") Integer userId, @PathVariable("cv-id") Integer cvId, @RequestBody UsersCvViewDto dto) throws JsonProcessingException {
        return ResponseEntity.ok(cvService.updateCvContact(userId, cvId, dto));
    }

    @GetMapping("/{user-id}/cv/{cv-id}/contact")
    @PreAuthorize("hasAnyAuthority('update:candidate', 'update:expert')")
    public ResponseEntity<?> getContactCv(@PathVariable("user-id") Integer userId, @PathVariable("cv-id") Integer cvId) throws JsonProcessingException {
        return ResponseEntity.ok(cvService.getCvContact(userId, cvId));
    }

    @GetMapping("/cv/{cv-id}/resume/title")
    @PreAuthorize("hasAnyAuthority('read:candidate', 'read:expert')")
    public ResponseEntity<?> getTitleResume(@PathVariable("cv-id") Integer cvId) {
        return ResponseEntity.ok(cvService.getTitleResume(cvId));
    }

    @PutMapping("/{cv-id}/target")
    @PreAuthorize("hasAnyAuthority('update:candidate', 'update:expert')")
    public String updateContact(@PathVariable("cv-id") int id, @RequestBody CvUpdateDto dto, Principal principal) {
        if (cvService.updateCvTarget(id, dto, principal)){
            return "Update success";
        } else throw new InternalServerException("Update Fail");
    }

    @DeleteMapping("/{user-id}/cv/{cv-id}")
    @PreAuthorize("hasAnyAuthority('update:candidate', 'update:expert')")
    public String deleteCv(@PathVariable("user-id") int UsersId, @PathVariable("cv-id") int cvId) {
        cvService.deleteCvById(UsersId, cvId);
        return "Delete successful";
    }

    @PutMapping("/{user-id}/cv/{cv-id}/public")
    @PreAuthorize("hasAnyAuthority('read:candidate', 'read:expert')")
    public String searchable(@PathVariable("user-id") int userId, @PathVariable("cv-id") int cvId) {
        boolean check = cvService.searchable(userId, cvId);
        if (check) {
            return "Public saved";
        } else {
            return "Public fail";
        }
    }

    @GetMapping("/cvs/public")
    @PreAuthorize("hasAnyAuthority('read:candidate', 'read:expert')")
    public List<CvAddNewDto> getSearchable(@RequestParam(required = false) String field) {
        return cvService.getListSearchable(field);
    }

    @GetMapping("/{user-id}/cvs/resume")
    @PreAuthorize("hasAnyAuthority('read:candidate', 'read:expert')")
    public List<CvResumeDto> getListResume(@PathVariable("user-id") Integer userId) {
        return cvService.getListResume(userId);
    }

    @GetMapping("/{user-id}/cv/{cv-id}/experiences/role-names")
    @PreAuthorize("hasAnyAuthority('read:candidate', 'read:expert')")
    public List<ExperienceRoleDto> getListExperienceRole(@PathVariable("user-id") Integer userId, @PathVariable("cv-id") Integer cvId) throws JsonProcessingException {
        return cvService.getListExperienceRole(userId,cvId);
    }


    @GetMapping("/{userId}/cv/{cvId}/micro")
    @PreAuthorize("hasAnyAuthority('read:candidate', 'read:expert')")
    public Cv findByUserIdAndId(@PathVariable("userId") Integer userId, @PathVariable("cvId") Integer cvId) throws JsonProcessingException {
        return cvService.findByUser_IdAndId(userId, cvId);
    }

    @GetMapping("/{user-id}/cv/detail/cover-letter")
    @PreAuthorize("hasAnyAuthority('read:candidate', 'read:expert')")
    public ResponseEntity<?> getCvsDetail(@PathVariable("user-id") Integer userId) {
        return ResponseEntity.ok(cvService.listCvDetail(userId));
    }
}
