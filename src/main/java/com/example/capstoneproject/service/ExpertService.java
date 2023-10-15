package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.ExpertUpdateDto;
import com.example.capstoneproject.Dto.responses.ExpertViewDto;
import org.springframework.stereotype.Service;

@Service
public interface ExpertService {
    ExpertViewDto getProfileExpert(Integer expertId);
    boolean updateExpert(Integer expertId, ExpertUpdateDto dto);
}
