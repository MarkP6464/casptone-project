package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.SkillViewDto;
import com.example.capstoneproject.service.SkillOfCvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cv")
public class SkillOfCvController {

    @Autowired
    SkillOfCvService skillOfCvService;

    public SkillOfCvController(SkillOfCvService skillOfCvService) {
        this.skillOfCvService = skillOfCvService;
    }

    @GetMapping("/{cvId}/skills/skill-of-cv")
    public List<SkillViewDto> getAllSkill(@PathVariable("cvId") int cvId) {
        return skillOfCvService.getActiveSkillsByCvId(cvId);
    }
    @GetMapping("/{cvId}/skills")
    public List<SkillViewDto> getAllSkillNotYet(@PathVariable("cvId") int cvId) {
        return skillOfCvService.getAllSkill(cvId);
    }
    @PostMapping("/{cvId}/skills/{skillId}")
    public String postSkillOfCv(@PathVariable("cvId") int cvId, @PathVariable("skillId") int skillId) {
        boolean check = skillOfCvService.createSkillOfCv(cvId, skillId);
        if(check){
            return "Addition success";
        }else {
            return "Addition fail";
        }
    }

    @DeleteMapping("/{cvId}/skills/{skillId}")
    public String deleteSkillOfCv(@PathVariable("cvId") int cvId, @PathVariable("skillId") int skillId) {
        boolean check = skillOfCvService.deleteSkillOfCv(cvId,skillId);
        if(check){
            return "Delete success";
        }else {
            return "Delete fail";
        }
    }
}
