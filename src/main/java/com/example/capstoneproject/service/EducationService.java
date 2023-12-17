package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.EducationDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EducationService extends BaseService<EducationDto, Integer> {
    EducationDto update(Integer id, EducationDto dto);

    boolean updateEducation(int UsersId, int educationId, EducationDto dto);

    List<EducationDto> getAllEducation(int UsersId);

    EducationDto createEducation(Integer id, EducationDto dto);

    void deleteEducationById(Integer UsersId, Integer educationId);

    EducationDto getAndIsDisplay(int cvId, int id) throws JsonProcessingException;

    EducationDto getByIdInCvBody(int cvId, int id) throws JsonProcessingException;

    List<EducationDto> getAllARelationInCvBody(int cvId) throws JsonProcessingException;

    boolean updateInCvBody(int cvId, int educationId, EducationDto dto) throws JsonProcessingException;


    EducationDto createOfUserInCvBody(int cvId, EducationDto dto) throws JsonProcessingException;

    void deleteInCvBody(Integer cvId, Integer educationId) throws JsonProcessingException;


}
