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

    boolean updateCertification(int customerId, int educationId, CertificationDto dto);
    List<CertificationViewDto> getAllCertification(int customerId);
    CertificationDto createCertification(Integer id, CertificationDto dto);
    void deleteCertificationById(Integer customerId,Integer certificationId);
}
