package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.ExpertDto;
import com.example.capstoneproject.Dto.ExpertUpdateDto;
import com.example.capstoneproject.Dto.responses.ExpertViewDto;
import com.example.capstoneproject.entity.Expert;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExpertService {
    ExpertViewDto getProfileExpert(Integer expertId);
    boolean updateExpert(Integer expertId, ExpertUpdateDto dto);
    List<ExpertViewDto> getExpertList();
}
