package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.EvaluateViewDto;
import com.example.capstoneproject.enums.SortOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public interface EvaluateService {
    List<BulletPointDto> checkSentences(String sentences);
    List<BulletPointDto> checkSentencesSecond(EvaluateDescriptionDto dto);
    List<AtsDto> ListAts(int cvId, int jobId, JobDescriptionDto dto, Principal principal) throws JsonProcessingException;
    String generationAts(String description) throws JsonProcessingException;

    List<AtsDto> getAts(int cvId, int jobId) throws JsonProcessingException;

    ScoreDto getEvaluateCv(int userId, int cvId) throws JsonProcessingException;

    String updateScoreEvaluate(Integer adminId, Integer evaluateId, EvaluateScoreDto dto);
    String updateCriteriaEvaluate(Integer adminId, Integer evaluateId, EvaluateCriteriaDto dto);
    List<EvaluateViewDto> viewEvaluate(Integer adminId, String search, SortOrder sort);

    String updateScoreAndCriteriaEvaluate(Integer adminId, Integer evaluateId, EvaluateScoreDto dto);
}
