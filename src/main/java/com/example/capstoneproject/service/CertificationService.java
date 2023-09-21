package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.CertificationDto;
import com.example.capstoneproject.Dto.CertificationViewDto;
import com.example.capstoneproject.Dto.EducationDto;
import com.example.capstoneproject.Dto.EducationViewDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CertificationService extends BaseService<CertificationDto, Integer> {
    CertificationDto update(Integer id, CertificationDto dto);

    boolean updateCertification(int UsersId, int educationId, CertificationDto dto);
    List<CertificationViewDto> getAllCertification(int UsersId);
    CertificationDto createCertification(Integer id, CertificationDto dto);
    void deleteCertificationById(Integer UsersId,Integer certificationId);
}
