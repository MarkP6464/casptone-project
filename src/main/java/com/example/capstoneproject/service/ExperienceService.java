package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExperienceService extends BaseService<ExperienceDto, Integer> {
    ExperienceDto update(Integer id, ExperienceDto dto);

    boolean updateExperience(int customerId, int experienceId, ExperienceDto dto);
    List<ExperienceViewDto> getAllExperience(int customerId);
    ExperienceDto createExperience(Integer id, ExperienceDto dto);
    void deleteExperienceById(Integer customerId,Integer experienceId);
}
