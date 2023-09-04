package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.EducationDto;
import org.springframework.stereotype.Service;

@Service
public interface EducationService extends BaseService<EducationDto, Integer> {
    EducationDto update(Integer id, EducationDto dto);
}
