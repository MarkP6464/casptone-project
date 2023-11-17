package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.ExpertTurnOnDto;
import com.example.capstoneproject.Dto.ExpertUpdateDto;
import com.example.capstoneproject.Dto.responses.ExpertReviewViewDto;
import com.example.capstoneproject.Dto.responses.ExpertViewChooseDto;
import com.example.capstoneproject.Dto.responses.ExpertViewDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ExpertService {
    boolean updateExpert(Integer expertId, ExpertUpdateDto dto);
    List<ExpertViewChooseDto> getExpertList(String search);
    ExpertReviewViewDto getDetailExpert(Integer expertId);
    boolean turnOnAvailability(Integer expertId, ExpertTurnOnDto dto);
    boolean turnOffAvailability(Integer expertId);
    void punishExpert(Integer expertId);
    void unPunishExpert();


}
