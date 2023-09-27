package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface ExperienceService extends BaseService<ExperienceDto, Integer> {
    ExperienceDto update(Integer id, ExperienceDto dto);

    boolean updateExperience(int UsersId, int experienceId, ExperienceDto dto);
    List<ExperienceDto> getAllExperience(int UsersId);
    ExperienceDto createExperience(Integer id, ExperienceDto dto);
    void deleteExperienceById(Integer UsersId,Integer experienceId);

    ExperienceDto getAndIsDisplay(int cvId, int id) throws JsonProcessingException;

    ExperienceDto getByIdInCvBody(int cvId, int id) throws JsonProcessingException;

    Set<ExperienceDto> getAllARelationInCvBody(int cvId) throws JsonProcessingException;

    boolean updateInCvBody(int cvId, int id, ExperienceDto dto) throws JsonProcessingException;

    ExperienceDto createOfUserInCvBody(int cvId, ExperienceDto dto) throws JsonProcessingException;

    void deleteInCvBody(Integer cvId, Integer educationId) throws JsonProcessingException;
}
