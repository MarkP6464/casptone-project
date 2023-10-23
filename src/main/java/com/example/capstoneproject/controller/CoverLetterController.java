package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.ChatResponse;
import com.example.capstoneproject.Dto.CoverLetterAddDto;
import com.example.capstoneproject.Dto.CoverLetterDto;
import com.example.capstoneproject.Dto.CoverLetterUpdateDto;
import com.example.capstoneproject.Dto.responses.CoverLetterViewDto;
import com.example.capstoneproject.service.impl.CoverLetterServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/chat-gpt")
public class CoverLetterController {

    @Autowired
    CoverLetterServiceImpl coverLetterService;


    public CoverLetterController(CoverLetterServiceImpl coverLetterService) {
        this.coverLetterService = coverLetterService;
    }

    @PostMapping("/cover-letter")
    public ResponseEntity<?> generateCoverLetter(
            @RequestParam float temperature,
            @RequestParam String company,
            @RequestParam String job_title,
            @RequestParam int cvId,
            @RequestParam String job_description
    ) throws JsonProcessingException {
        if (temperature < 0.2 || temperature > 1.0) {
            return ResponseEntity.badRequest().body("Temperature value is invalid. Must be between 0.2 and 1.0.");
        }

        ChatResponse result = coverLetterService.generateCoverLetter(
                temperature,
                job_title,
                cvId,
                company,
                job_description
        );
        return ResponseEntity.ok(result);
    }

    @PostMapping("/cover-letter/buzz")
    public ChatResponse generateCoverLetterRevi(
            @RequestParam float temp,
            @RequestParam String content
    ) throws JsonProcessingException {

        ChatResponse result = coverLetterService.generateEvaluate(
                temp,
                content
        );
        return result;
    }


    @PostMapping("/cover-letter/revise")
    public ChatResponse generateCoverLetterRevise(
            @RequestParam String content,
            @RequestParam String improvement
    ) throws JsonProcessingException {

        ChatResponse result = coverLetterService.reviseCoverLetter(
                content,
                improvement
        );
        return result;
    }

    @PostMapping("/{UsersId}/cover-letter")
    public CoverLetterViewDto createCoverLetter(@PathVariable("UsersId") int UsersId, @RequestBody CoverLetterAddDto Dto) {
        return coverLetterService.createCoverLetter(UsersId, Dto);
    }

    @PutMapping("/{UsersId}/cover-letter/{coverLetterId}")
    public String updateCoverLetter(@PathVariable("UsersId") int UsersId, @PathVariable("coverLetterId") int coverLetterId, @RequestBody CoverLetterUpdateDto Dto) {
        boolean check = coverLetterService.updateCoverLetter(UsersId, coverLetterId, Dto);
        if (check) {
            return "Changes saved";
        } else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{UsersId}/cover-letter/{coverLetterId}")
    public String deleteCoverLetter(@PathVariable("UsersId") int UsersId, @PathVariable("coverLetterId") int coverLetterId) {
        boolean check = coverLetterService.deleteCoverLetterById(UsersId, coverLetterId);
        if (check) {
            return "Delete success";
        } else {
            return "Delete fail";
        }
    }

    @GetMapping("/{UsersId}/cover-letter/{coverLetterId}")
    public CoverLetterDto getCoverLetter(@PathVariable("UsersId") int UsersId, @PathVariable("coverLetterId") int coverLetterId) {
        return coverLetterService.getCoverLetter(UsersId, coverLetterId);
    }

}
