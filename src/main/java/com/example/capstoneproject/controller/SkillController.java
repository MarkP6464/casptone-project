package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.ProjectDto;
import com.example.capstoneproject.Dto.SkillDto;
import com.example.capstoneproject.service.ProjectService;
import com.example.capstoneproject.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cv/skills")
public class SkillController {
    @Autowired
    SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public List<SkillDto> getAllSkill() {
        return skillService.getAll();
    }

    @PostMapping
    public SkillDto postSkill(@RequestBody SkillDto Dto) {
        return skillService.create(Dto);
    }

    @PutMapping("/{skillId}")
    public SkillDto updateSkill(@PathVariable("skillId") int skillId, @RequestBody SkillDto Dto) {
        return skillService.update(skillId, Dto);
    }

    @DeleteMapping("/{skillId}")
    public String deleteProject(@PathVariable("skillId") int skillId) {
        skillService.deleteById(skillId);
        return "Delete successful";
    }
}
