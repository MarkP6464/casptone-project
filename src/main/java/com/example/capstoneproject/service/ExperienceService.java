package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExperienceService extends BaseService<ExperienceDto, Integer> {
    ExperienceDto update(Integer id, ExperienceDto dto);

    boolean updateExperience(int UsersId, int experienceId, ExperienceDto dto);
    List<ExperienceViewDto> getAllExperience(int UsersId);
    ExperienceDto createExperience(Integer id, ExperienceDto dto);
    void deleteExperienceById(Integer UsersId,Integer experienceId);
}
