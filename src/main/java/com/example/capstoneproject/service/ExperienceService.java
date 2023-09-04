package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.ExperienceDto;
import org.springframework.stereotype.Service;

@Service
public interface ExperienceService extends BaseService<ExperienceDto, Integer> {
    ExperienceDto update(Integer id, ExperienceDto dto);
}
