package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.EducationOfCvDto;
import com.example.capstoneproject.entity.EducationOfCv;
import org.springframework.stereotype.Component;

@Component
public class EducationOfCvMapper extends AbstractMapper<EducationOfCv, EducationOfCvDto> {
    public EducationOfCvMapper() {
        super(EducationOfCv.class, EducationOfCvDto.class);
    }
}
