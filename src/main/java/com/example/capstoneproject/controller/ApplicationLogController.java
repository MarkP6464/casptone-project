package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.NoteDto;
import com.example.capstoneproject.Dto.responses.ApplicationLogFullResponse;
import com.example.capstoneproject.Dto.responses.ApplicationLogJobResponse;
import com.example.capstoneproject.Dto.responses.ApplicationLogResponse;
import com.example.capstoneproject.service.ApplicationLogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ApplicationLogController {

    @Autowired
    ApplicationLogService applicationLogService;

    public ApplicationLogController(ApplicationLogService applicationLogService) {
        this.applicationLogService = applicationLogService;
    }

    @PostMapping("/user/{user-id}/cv/{cv-id}/job-posting/{posting-id}/apply")
    @PreAuthorize("hasAuthority('create:candidate')")
    public ResponseEntity<?> createApplication(@PathVariable("user-id") Integer userId, @PathVariable("cv-id") Integer cvId, @PathVariable("posting-id") Integer postingId, @RequestParam(required = false) Integer cover_letter_id, NoteDto dto) throws JsonProcessingException {
        if (cover_letter_id == null) {
            if (applicationLogService.applyCvToPost(userId, cvId, null, postingId, dto)) {
                return ResponseEntity.ok("Apply success");
            } else {
                return ResponseEntity.badRequest().body("Apply failed");
            }
        }
        if (applicationLogService.applyCvToPost(userId, cvId, cover_letter_id, postingId, dto)) {
            return ResponseEntity.ok("Apply success");
        } else {
            return ResponseEntity.badRequest().body("Apply failed");
        }
    }


    @GetMapping("/application-log/{post-id}")
    @PreAuthorize("hasAnyAuthority('read:hr')")
    public ResponseEntity<List<ApplicationLogJobResponse>> getAllLog(@PathVariable("post-id") Integer postId){
        return ResponseEntity.ok(applicationLogService.getAll(postId));
    }

    @GetMapping("/application-log/hr/{hr-id}")
    @PreAuthorize("hasAnyAuthority('read:hr')")
    public List<ApplicationLogFullResponse> getAllLogByHrId(@PathVariable("hr-id") Integer hrId){
        List<ApplicationLogFullResponse> list =  applicationLogService.getAllByHrID(hrId);
        return list;
    }

    @GetMapping("/application-log/candidate/{candidate-id}")
    @PreAuthorize("hasAnyAuthority('read:candidate', 'read:hr')")
    public ResponseEntity<?> getAllLogByCandidateId(@PathVariable("candidate-id") Integer id){
        return ResponseEntity.ok(applicationLogService.getAllByCandidateId(id));
    }

    @GetMapping("/application-log/{log-id}/downloaded")
    @PreAuthorize("hasAnyAuthority('read:candidate')")
    public ApplicationLogResponse updateStatusDownloaded(@PathVariable("log-id") Integer id){
        ApplicationLogResponse list =  applicationLogService.updateDownloaded(id);
        return list;
    }
    @GetMapping("/application-log/{log-id}/seen")
    @PreAuthorize("hasAnyAuthority('read:candidate')")
    public ApplicationLogResponse updateStatusSeen(@PathVariable("log-id") Integer id){
        ApplicationLogResponse list =  applicationLogService.updateSeen(id);
        return list;
    }
}
