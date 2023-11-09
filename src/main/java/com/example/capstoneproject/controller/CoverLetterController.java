package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.ChatResponse;
import com.example.capstoneproject.Dto.CoverLetterAddDto;
import com.example.capstoneproject.Dto.CoverLetterDto;
import com.example.capstoneproject.Dto.CoverLetterUpdateDto;
import com.example.capstoneproject.Dto.responses.CoverLetterViewDto;
import com.example.capstoneproject.service.impl.CoverLetterServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("cv/{cv-id}/summary")
    public ResponseEntity<?> generateSummary(
            @RequestParam float temperature,
            @PathVariable("cv-id") Integer cvId,
            @RequestParam String position_highlight,
            @RequestParam String skill_highlight
    ) throws JsonProcessingException {
        if (temperature < 0.2 || temperature > 1.0) {
            return ResponseEntity.badRequest().body("Temperature value is invalid. Must be between 0.2 and 1.0.");
        }

        ChatResponse result = coverLetterService.generateSummaryCV(
                temperature,
                cvId,
                position_highlight,
                skill_highlight
        );
        return ResponseEntity.ok(result);
    }

    @PostMapping("/cv/{cv-id}/review")
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

    @PostMapping("/user/{user-id}/cv/{cv-id}/cover-letter")
    public CoverLetterViewDto createCoverLetter(@PathVariable("user-id") Integer userId,@PathVariable("cv-id") Integer cvId, @RequestBody CoverLetterAddDto Dto) {
        return coverLetterService.createCoverLetter(userId, cvId, Dto);
    }

    @PutMapping("/user/cv/{cv-id}/cover-letter/{cover-letter-id}")
    public String updateCoverLetter(@PathVariable("cv-id") int cvId, @PathVariable("cover-letter-id") int coverLetterId, @RequestBody CoverLetterUpdateDto Dto) {
        boolean check = coverLetterService.updateCoverLetter(cvId, coverLetterId, Dto);
        if (check) {
            return "Changes saved";
        } else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/user/{user-id}/cover-letter/{cover-letter-id}")
    public String deleteCoverLetter(@PathVariable("user-id") int UsersId, @PathVariable("cover-letter-id") int coverLetterId) {
        boolean check = coverLetterService.deleteCoverLetterById(UsersId, coverLetterId);
        if (check) {
            return "Delete success";
        } else {
            return "Delete fail";
        }
    }

    @GetMapping("/user/{user-id}/cover-letter/{cover-letter-id}")
    public CoverLetterDto getCoverLetter(@PathVariable("user-id") int UsersId, @PathVariable("cover-letter-id") int coverLetterId) {
        return coverLetterService.getCoverLetter(UsersId, coverLetterId);
    }

}
