package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.InvolvementViewDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvolvementService extends BaseService<InvolvementDto, Integer> {
    boolean updateInvolvement(int UsersId, int involvementId, InvolvementDto dto);

    List<InvolvementDto> getAllInvolvement(int UsersId);

    InvolvementDto createInvolvement(Integer id, InvolvementDto dto);

    void deleteInvolvementById(Integer UsersId, Integer involvementId);

    InvolvementViewDto getAndIsDisplay(int cvId, int id) throws JsonProcessingException;

    InvolvementDto getByIdInCvBody(int cvId, int id) throws JsonProcessingException;

    List<InvolvementViewDto> getAllARelationInCvBody(int cvId) throws JsonProcessingException;

    InvolvementViewDto updateInCvBody(int cvId, int id, InvolvementDto dto) throws JsonProcessingException;

    InvolvementViewDto createOfUserInCvBody(int cvId, InvolvementDto dto) throws JsonProcessingException;

    void deleteInCvBody(Integer cvId, Integer id) throws JsonProcessingException;
}
