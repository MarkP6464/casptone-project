package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.ExperienceOfCvDto;
import com.example.capstoneproject.entity.ExperienceOfCv;
import org.springframework.stereotype.Component;

@Component
public class ExperienceOfCvMapper extends AbstractMapper<ExperienceOfCv, ExperienceOfCvDto> {
    public ExperienceOfCvMapper() {
        super(ExperienceOfCv.class, ExperienceOfCvDto.class);
    }
}
