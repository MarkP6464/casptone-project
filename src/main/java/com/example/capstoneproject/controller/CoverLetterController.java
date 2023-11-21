package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.CoverLetterViewDto;
import com.example.capstoneproject.service.impl.CoverLetterServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat-gpt")
public class CoverLetterController {

    @Autowired
    CoverLetterServiceImpl coverLetterService;


    public CoverLetterController(CoverLetterServiceImpl coverLetterService) {
        this.coverLetterService = coverLetterService;
    }

    @PostMapping("/cover-letter/{cover-letter-id}/generation")
    @PreAuthorize("hasAuthority('create:candidate')")
    public ResponseEntity<?> generateCoverLetter(
            @PathVariable("cover-letter-id") Integer coverId,
            @RequestParam float temperature,
            @RequestParam int cvId,
            CoverLetterGenerationDto dto
    ) throws JsonProcessingException {
        if (temperature < 0.2 || temperature > 1.0) {
            return ResponseEntity.badRequest().body("Temperature value is invalid. Must be between 0.2 and 1.0.");
        }

        ChatResponse result = coverLetterService.generateCoverLetter(
                coverId,
                temperature,
                cvId,
                dto

        );
        return ResponseEntity.ok(result);
    }

    @PostMapping("cv/{cv-id}/summary")
    @PreAuthorize("hasAnyAuthority('create:candidate','create:expert')")
    public ResponseEntity<?> generateSummary(
            @PathVariable("cv-id") Integer cvId,
            SummaryGenerationDto dto
    ) throws JsonProcessingException {
        ChatResponse result = coverLetterService.generateSummaryCV(
                cvId,
                dto
        );
        return ResponseEntity.ok(result);
    }

    @PostMapping("/cv/{cv-id}/review")
    @PreAuthorize("hasAnyAuthority('create:candidate','create:expert')")
    public ResponseEntity<?> reviewCv(
            @RequestParam float temperature,
            @PathVariable("cv-id") Integer cvId
    ) throws JsonProcessingException {
        if (temperature < 0.2 || temperature > 1.0) {
            return ResponseEntity.badRequest().body("Temperature value is invalid. Must be between 0.2 and 1.0.");
        }

        ChatResponse result = coverLetterService.reviewCV(
                temperature,
                cvId
        );
        return ResponseEntity.ok(result);
    }

//    @PostMapping(value = "/checkBuzz", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<String> generatedCoverLetter(
//            @RequestParam float temperature,
//            @RequestParam String description
//    ) {
//        if (temperature < 0.2 || temperature > 1.0) {
//            return Flux.just("Temperature value is invalid. Must be between 0.2 and 1.0.");
//        }
//
//        //return coverLetterService.generateEvaluate(temperature, description);
//        return null;
//    }


    @PostMapping("/cover-letter/revise")
    @PreAuthorize("hasAuthority('create:candidate')")
    public ChatResponse generateCoverLetterRevise(
            CoverLetterReviseDto dto
    ) throws JsonProcessingException {

        ChatResponse result = coverLetterService.reviseCoverLetter(
                dto
        );
        return result;
    }

    @PostMapping("/user/{user-id}/cv/cover-letter")
    @PreAuthorize("hasAuthority('create:candidate')")
    public CoverLetterViewDto createCoverLetter(@PathVariable("user-id") Integer userId, @RequestBody CoverLetterAddDto Dto) {
        return coverLetterService.createCoverLetter(userId, Dto);
    }

    @PutMapping("/user/cv/{cv-id}/cover-letter/{cover-letter-id}")
    @PreAuthorize("hasAuthority('update:candidate')")
    public String updateCoverLetter(@PathVariable("cv-id") int cvId, @PathVariable("cover-letter-id") int coverLetterId, @RequestBody CoverLetterUpdateDto Dto) {
        boolean check = coverLetterService.updateCoverLetter(cvId, coverLetterId, Dto);
        if (check) {
            return "Changes saved";
        } else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/user/{user-id}/cover-letter/{cover-letter-id}")
    @PreAuthorize("hasAuthority('delete:candidate')")
    public String deleteCoverLetter(@PathVariable("user-id") int UsersId, @PathVariable("cover-letter-id") int coverLetterId) {
        boolean check = coverLetterService.deleteCoverLetterById(UsersId, coverLetterId);
        if (check) {
            return "Delete success";
        } else {
            return "Delete fail";
        }
    }

    @GetMapping("/user/{user-id}/cover-letter/{cover-letter-id}")
    @PreAuthorize("hasAuthority('read:candidate')")
    public CoverLetterDto getCoverLetter(@PathVariable("user-id") int UsersId, @PathVariable("cover-letter-id") int coverLetterId) {
        return coverLetterService.getCoverLetter(UsersId, coverLetterId);
    }

}
