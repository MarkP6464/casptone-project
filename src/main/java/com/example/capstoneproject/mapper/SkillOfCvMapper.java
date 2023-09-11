package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.SkillOfCvDto;
import com.example.capstoneproject.entity.SkillOfCv;
import org.springframework.stereotype.Component;

@Component
public class SkillOfCvMapper extends AbstractMapper<SkillOfCv, SkillOfCvDto> {
    public SkillOfCvMapper() {
        super(SkillOfCv.class, SkillOfCvDto.class);
    }
}
