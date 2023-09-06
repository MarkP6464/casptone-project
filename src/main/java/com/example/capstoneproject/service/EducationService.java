package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.CertificationViewDto;
import com.example.capstoneproject.Dto.EducationDto;
import com.example.capstoneproject.Dto.EducationViewDto;
import com.example.capstoneproject.Dto.ExperienceViewDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EducationService extends BaseService<EducationDto, Integer> {
    EducationDto update(Integer id, EducationDto dto);

    boolean updateEducation(Integer id, EducationViewDto dto);
    List<EducationViewDto> getAllEducation(int cvId);
}
