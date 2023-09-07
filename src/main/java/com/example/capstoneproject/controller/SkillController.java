package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.ProjectDto;
import com.example.capstoneproject.Dto.SkillDto;
import com.example.capstoneproject.Dto.SkillViewDto;
import com.example.capstoneproject.service.ProjectService;
import com.example.capstoneproject.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cv")
public class SkillController {
    @Autowired
    SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("/{cvId}/skills")
    public List<SkillViewDto> getAllSkill(@PathVariable("cvId") int cvId) {
        return skillService.getAllSkill(cvId);
    }

    @PostMapping("/skills")
    public SkillDto postSkill(@RequestBody SkillDto Dto) {
        return skillService.create(Dto);
    }

    @PutMapping("/skills/{skillId}")
    public String updateSkill(@PathVariable("skillId") int skillId, @RequestBody SkillViewDto Dto) {
        boolean check = skillService.updateSkill(skillId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/skills/{skillId}")
    public String deleteProject(@PathVariable("skillId") int skillId) {
        skillService.deleteById(skillId);
        return "Delete successful";
    }
}
