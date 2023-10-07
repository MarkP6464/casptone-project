package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EvaluateService {
    List<BulletPointDto> checkSentences(String sentences);
    List<AtsDto> ListAts(int cvId, int jobId, JobDescriptionDto dto) throws JsonProcessingException;

    List<AtsDto> getAts(int cvId, int jobId) throws JsonProcessingException;
}