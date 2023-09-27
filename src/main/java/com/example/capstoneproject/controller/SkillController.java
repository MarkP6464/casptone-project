package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.SkillDto;
import com.example.capstoneproject.Dto.responses.SkillViewDto;
import com.example.capstoneproject.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/Users")
public class SkillController {
    @Autowired
    SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("/{UsersId}/skills")
    public List<SkillViewDto> getAllSkill(@PathVariable("UsersId") int UsersId) {
        return skillService.getAllSkill(UsersId);
    }

    @PostMapping("/{UsersId}/skills")
    public SkillDto postSkill(@PathVariable("UsersId") int UsersId,@RequestBody SkillDto Dto) {
        return skillService.createSkill(UsersId,Dto);
    }

    @PutMapping("/{UsersId}/skills/{skillId}")
    public String updateSkill(@PathVariable("UsersId") int UsersId,@PathVariable("skillId") int skillId, @RequestBody SkillDto Dto) {
        boolean check = skillService.updateSkill(UsersId,skillId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{UsersId}/skills/{skillId}")
    public String deleteProject(@PathVariable("UsersId") int UsersId,@PathVariable("skillId") int skillId) {
        skillService.deleteSkillById(UsersId,skillId);
        return "Delete successful";
    }
}
