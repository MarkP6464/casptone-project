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

    @PostMapping("/{cvId}/skills")
    public SkillDto postSkill(@PathVariable("cvId") int cvId,@RequestBody SkillDto Dto) {
        return skillService.createSkill(cvId,Dto);
    }

    @PutMapping("/{cvId}/skills/{skillId}")
    public String updateSkill(@PathVariable("cvId") int cvId,@PathVariable("skillId") int skillId, @RequestBody SkillDto Dto) {
        boolean check = skillService.updateSkill(cvId,skillId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{cvId}/skills/{skillId}")
    public String deleteProject(@PathVariable("cvId") int cvId,@PathVariable("skillId") int skillId) {
        skillService.deleteSkillById(cvId,skillId);
        return "Delete successful";
    }
}
