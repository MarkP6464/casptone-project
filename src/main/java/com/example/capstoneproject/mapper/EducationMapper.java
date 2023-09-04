package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.CertificationDto;
import com.example.capstoneproject.Dto.EducationDto;
import com.example.capstoneproject.entity.Certification;
import com.example.capstoneproject.entity.Education;
import org.springframework.stereotype.Component;

@Component
public class EducationMapper extends AbstractMapper<Education, EducationDto> {
    public EducationMapper() {
        super(Education.class, EducationDto.class);
    }
}
