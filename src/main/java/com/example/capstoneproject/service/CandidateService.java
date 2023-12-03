package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.CandidateDto;
import com.example.capstoneproject.Dto.CvAddNewDto;
import com.example.capstoneproject.Dto.responses.CandidateOverViewDto;
import com.example.capstoneproject.Dto.responses.CandidateViewDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CandidateService {
    boolean updateCandidate(Integer candidateId, CandidateDto dto);
    CandidateViewDto getCandidateConfig(Integer candidateId);
    List<CandidateOverViewDto> getAllCandidatePublish(String search);
    List<CvAddNewDto> getAllCvPublishCandidate(Integer candidateId) throws JsonProcessingException;
}
