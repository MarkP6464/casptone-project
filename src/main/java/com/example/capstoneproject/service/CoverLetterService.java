package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.CoverLetterViewDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public interface CoverLetterService extends BaseService<CoverLetterDto, Integer>{
    ChatResponse generateCoverLetter(Integer coverId,  Integer cvId, CoverLetterGenerationDto dto, Principal principal) throws JsonProcessingException;
    ChatResponse generateSummaryCV(Integer cvId, SummaryGenerationDto dto, Principal principal) throws JsonProcessingException;
    ChatResponse reviewCV(float temperature, Integer cvId, Principal principal) throws JsonProcessingException;
    CoverLetterViewDto createCoverLetter(Integer userId, Integer cvId, CoverLetterAddDto dto);
    List<CoverLetterViewDto> getAllCoverLetter(Integer userId);
    boolean updateCoverLetter(Integer cvId, Integer coverLetterId, CoverLetterUpdateDto dto);
    boolean deleteCoverLetterById(Integer UsersId,Integer coverLetterId);
    CoverLetterDto getCoverLetter(Integer userId, Integer coverLetterId);
    ChatResponse reviseCoverLetter(CoverLetterReviseDto dto, Principal principal) throws JsonProcessingException;
    ChatResponseArray rewritteExperience(ReWritterExperienceDto dto, Principal principal) throws JsonProcessingException;

}
