package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.ExperienceOfCvDto;
import com.example.capstoneproject.Dto.ExperienceViewDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExperienceOfCvService extends BaseService<ExperienceOfCvDto, Integer> {
    boolean createExperienceOfCv(Integer cvId, Integer experienceId);
    boolean deleteExperienceOfCv(Integer cvId, Integer experienceId);
    List<ExperienceViewDto> getActiveExperiencesByCvId(Integer cvId);
    List<ExperienceViewDto> getAllExperience(int cvId);
}
