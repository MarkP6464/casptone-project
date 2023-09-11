package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EducationService extends BaseService<EducationDto, Integer> {
    EducationDto update(Integer id, EducationDto dto);

    boolean updateEducation(int customerId, int educationId, EducationDto dto);
    List<EducationViewDto> getAllEducation(int customerId);
    EducationDto createEducation(Integer id, EducationDto dto);
    void deleteEducationById(Integer customerId,Integer educationId);
}
