package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.CoverLetterAddDto;
import com.example.capstoneproject.Dto.CoverLetterDto;
import com.example.capstoneproject.Dto.CoverLetterUpdateDto;
import com.example.capstoneproject.Dto.EducationDto;
import com.example.capstoneproject.Dto.responses.CoverLetterViewDto;
import com.example.capstoneproject.service.impl.CoverLetterServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chat-gpt")
public class CoverLetterController {

    @Autowired
    CoverLetterServiceImpl coverLetterService;

    public CoverLetterController(CoverLetterServiceImpl coverLetterService) {
        this.coverLetterService = coverLetterService;
    }

    @PostMapping("/cover-letter")
    public String generateCoverLetter(
            @RequestParam float temperature,
            @RequestParam String company,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String dear,
            @RequestParam String name,
            @RequestParam String description
    ) throws JsonProcessingException {
        if (temperature < 0.2 || temperature > 1.0) {
            return "Temperature value is invalid. Must be between 0.2 and 1.0.";
        }

        String result = coverLetterService.generateCoverLetter(
                temperature,
                title,
                content,
                dear,
                name,
                company,
                description
        );
        return result;
    }

    @PostMapping("/{UsersId}/cover-letter")
    public CoverLetterViewDto createCoverLetter(@PathVariable("UsersId") int UsersId, @RequestBody CoverLetterAddDto Dto) {
        return coverLetterService.createCoverLetter(UsersId, Dto);
    }

    @PutMapping("/{UsersId}/cover-letter/{coverLetterId}")
    public String updateCoverLetter(@PathVariable("UsersId") int UsersId,@PathVariable("coverLetterId") int coverLetterId, @RequestBody CoverLetterUpdateDto Dto) {
        boolean check = coverLetterService.updateCoverLetter(UsersId,coverLetterId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{UsersId}/cover-letter/{coverLetterId}")
    public String deleteCoverLetter(@PathVariable("UsersId") int UsersId,@PathVariable("coverLetterId") int coverLetterId) {
        boolean check = coverLetterService.deleteCoverLetterById(UsersId,coverLetterId);
        if(check){
            return "Delete success";
        }else {
            return "Delete fail";
        }
    }

    @GetMapping("/{UsersId}/cover-letter/{coverLetterId}")
    public CoverLetterDto getCoverLetter(@PathVariable("UsersId") int UsersId, @PathVariable("coverLetterId") int coverLetterId) {
        return coverLetterService.getCoverLetter(UsersId,coverLetterId);
    }

}
