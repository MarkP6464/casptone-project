package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.ProjectDto;
import com.example.capstoneproject.Dto.SkillDto;
import com.example.capstoneproject.entity.Project;
import com.example.capstoneproject.entity.Skill;
import org.springframework.stereotype.Component;

@Component
public class SkillMapper extends AbstractMapper<Skill, SkillDto> {
    public SkillMapper() {
        super(Skill.class, SkillDto.class);
    }
}
