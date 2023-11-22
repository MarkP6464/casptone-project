package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.CandidateDto;
import com.example.capstoneproject.Dto.responses.CandidateOverViewDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CandidateService {
    boolean updateCandidate(Integer candidateId, CandidateDto dto);
    CandidateDto getCandidateConfig(Integer candidateId);
    List<CandidateOverViewDto> getAllCandidatePublish(String search);
}
