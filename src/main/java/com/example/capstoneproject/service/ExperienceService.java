package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.EducationViewDto;
import com.example.capstoneproject.Dto.ExperienceDto;
import com.example.capstoneproject.Dto.ExperienceViewDto;
import com.example.capstoneproject.Dto.InvolvementViewDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExperienceService extends BaseService<ExperienceDto, Integer> {
    ExperienceDto update(Integer id, ExperienceDto dto);

    boolean updateExperience(Integer id, ExperienceViewDto dto);
    List<ExperienceViewDto> getAllExperience(int cvId);
}
