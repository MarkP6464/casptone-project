package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.ExperienceDto;
import com.example.capstoneproject.Dto.request.HRBankRequest;
import com.example.capstoneproject.entity.Experience;
import com.example.capstoneproject.entity.Expert;
import com.example.capstoneproject.entity.HR;
import org.springframework.stereotype.Component;

@Component
public class ExperienceMapper extends AbstractMapper<Experience, ExperienceDto> {
    public ExperienceMapper() {
        super(Experience.class, ExperienceDto.class);
    }

}
