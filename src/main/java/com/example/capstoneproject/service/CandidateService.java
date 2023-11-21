package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.CandidateDto;
import org.springframework.stereotype.Service;

@Service
public interface CandidateService {
    boolean updateCandidate(Integer candidateId, CandidateDto dto);
    CandidateDto getCandidateConfig(Integer candidateId);
}
