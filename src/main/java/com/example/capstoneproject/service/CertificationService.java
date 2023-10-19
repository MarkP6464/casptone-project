package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.CertificationDto;
import com.example.capstoneproject.Dto.responses.CertificationViewDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CertificationService extends BaseService<CertificationDto, Integer> {
    CertificationDto update(Integer id, CertificationDto dto);

    boolean updateCertification(int UsersId, int educationId, CertificationDto dto);

    List<CertificationViewDto> getAllCertification(int UsersId);

    CertificationDto createCertification(Integer id, CertificationDto dto);

    void deleteCertificationById(Integer UsersId, Integer certificationId);

    CertificationDto getAndIsDisplay(int cvId, int id) throws JsonProcessingException;

    CertificationDto getByIdInCvBody(int cvId, int id) throws JsonProcessingException;

    List<CertificationDto> getAllARelationInCvBody(int cvId) throws JsonProcessingException;

    boolean updateInCvBody(int cvId, int id, CertificationDto dto) throws JsonProcessingException;

    CertificationDto createOfUserInCvBody(int cvId, CertificationDto dto) throws JsonProcessingException;

    void deleteInCvBody(Integer cvId, Integer educationId) throws JsonProcessingException;
}
