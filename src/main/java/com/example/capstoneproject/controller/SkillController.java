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
@RequestMapping("/api/v1/customer")
public class SkillController {
    @Autowired
    SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("/{customerId}/skills")
    public List<SkillViewDto> getAllSkill(@PathVariable("customerId") int customerId) {
        return skillService.getAllSkill(customerId);
    }

    @PostMapping("/{customerId}/skills")
    public SkillDto postSkill(@PathVariable("customerId") int customerId,@RequestBody SkillDto Dto) {
        return skillService.createSkill(customerId,Dto);
    }

    @PutMapping("/{customerId}/skills/{skillId}")
    public String updateSkill(@PathVariable("customerId") int customerId,@PathVariable("skillId") int skillId, @RequestBody SkillDto Dto) {
        boolean check = skillService.updateSkill(customerId,skillId, Dto);
        if(check){
            return "Changes saved";
        }else {
            return "Changes fail";
        }
    }

    @DeleteMapping("/{customerId}/skills/{skillId}")
    public String deleteProject(@PathVariable("customerId") int customerId,@PathVariable("skillId") int skillId) {
        skillService.deleteSkillById(customerId,skillId);
        return "Delete successful";
    }
}
