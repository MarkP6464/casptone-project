package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.ExperienceDto;
import com.example.capstoneproject.Dto.responses.ExperienceViewDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExperienceService extends BaseService<ExperienceDto, Integer> {
    ExperienceDto update(Integer id, ExperienceDto dto);

    boolean updateExperience(Integer cvId, Integer experienceId, ExperienceDto dto);

    List<ExperienceDto> getAllExperience(Integer cvId);
    ExperienceViewDto createExperience(Integer id, ExperienceDto dto);
    void deleteExperienceById(Integer UsersId,Integer experienceId);

    ExperienceViewDto getAndIsDisplay(int cvId, int id) throws JsonProcessingException;

    ExperienceDto getByIdInCvBody(int cvId, int id) throws JsonProcessingException;

    List<ExperienceViewDto> getAllARelationInCvBody(int cvId) throws JsonProcessingException;

    ExperienceViewDto updateInCvBody(int cvId, int id, ExperienceDto dto) throws JsonProcessingException;

    ExperienceViewDto createOfUserInCvBody(int cvId, ExperienceDto dto) throws JsonProcessingException;

    void deleteInCvBody(Integer cvId, Integer educationId) throws JsonProcessingException;
}
