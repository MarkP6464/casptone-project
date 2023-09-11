package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.CertificationOfCvDto;
import com.example.capstoneproject.Dto.CertificationViewDto;
import com.example.capstoneproject.Dto.EducationOfCvDto;
import com.example.capstoneproject.Dto.EducationViewDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EducationOfCvService extends BaseService<EducationOfCvDto, Integer> {
    boolean createEducationOfCv(Integer cvId, Integer educationId);
    boolean deleteEducationOfCv(Integer cvId, Integer educationId);
    List<EducationViewDto> getActiveEducationsByCvId(Integer cvId);
    List<EducationViewDto> getAllEducation(int cvId);
}
