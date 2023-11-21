package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.ExpertUpdateDto;
import com.example.capstoneproject.Dto.responses.ExpertConfigViewDto;
import com.example.capstoneproject.Dto.responses.ExpertReviewViewDto;
import com.example.capstoneproject.Dto.responses.ExpertViewChooseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExpertService {
    boolean updateExpert(Integer expertId, Integer cvId, ExpertUpdateDto dto) throws JsonProcessingException;
    ExpertConfigViewDto getExpertConfig(Integer expertId);
    List<ExpertViewChooseDto> getExpertList(String search);
    ExpertReviewViewDto getDetailExpert(Integer expertId);
    void punishExpert(Integer expertId);
    void unPunishExpert();


}
