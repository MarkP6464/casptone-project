package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.CertificationOfCvDto;
import com.example.capstoneproject.Dto.CertificationViewDto;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface CertificationOfCvService extends BaseService<CertificationOfCvDto, Integer> {
    boolean createCertificationOfCv(Integer cvId, Integer certificationId);
    boolean deleteCertificationOfCv(Integer cvId, Integer certificationId);
    List<CertificationViewDto> getActiveCertificationsByCvId(Integer cvId);
    List<CertificationViewDto> getAllCertification(int cvId);
}
