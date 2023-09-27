package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.SourceWorkDto;
import com.example.capstoneproject.Dto.responses.SourceWorkViewDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface SourceWorkService extends BaseService<SourceWorkDto, Integer> {
    SourceWorkDto update(Integer id, SourceWorkDto dto);

    boolean updateSourceWork(int UsersId, int sourceWorkId, SourceWorkDto dto);

    List<SourceWorkViewDto> getAllSourceWork(int UsersId);
    SourceWorkDto createSourceWork(Integer id, SourceWorkDto dto);
    void deleteSourceWorkById(Integer UsersId,Integer sourceId);

    SourceWorkDto getAndIsDisplay(int cvId, int id) throws JsonProcessingException;

    SourceWorkDto getByIdInCvBody(int cvId, int id) throws JsonProcessingException;

    Set<SourceWorkDto> getAllARelationInCvBody(int cvId) throws JsonProcessingException;

    boolean updateInCvBody(int cvId, int id, SourceWorkDto dto) throws JsonProcessingException;

    SourceWorkDto createOfUserInCvBody(int cvId, SourceWorkDto dto) throws JsonProcessingException;

    void deleteInCvBody(Integer cvId, Integer id) throws JsonProcessingException;
}
