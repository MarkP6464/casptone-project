package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.SkillDto;
import com.example.capstoneproject.Dto.SourceWorkDto;
import com.example.capstoneproject.entity.Skill;
import com.example.capstoneproject.entity.SourceWork;
import org.springframework.stereotype.Component;

@Component
public class SourceWorkMapper extends AbstractMapper<SourceWork, SourceWorkDto> {
    public SourceWorkMapper() {
        super(SourceWork.class, SourceWorkDto.class);
    }
}
