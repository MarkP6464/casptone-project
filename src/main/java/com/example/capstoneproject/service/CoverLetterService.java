package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.CoverLetterViewDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public interface CoverLetterService extends BaseService<CoverLetterDto, Integer>{
    CoverLetterViewDto createCoverLetter(Integer userId, Integer cvId, CoverLetterAddDto dto);
    List<CoverLetterViewDto> getAllCoverLetter(Integer userId);
    boolean updateCoverLetter(Integer cvId, Integer coverLetterId, CoverLetterUpdateDto dto);
    boolean deleteCoverLetterById(Integer UsersId,Integer coverLetterId);
    CoverLetterDto getCoverLetter(Integer userId, Integer coverLetterId);
    ChatResponse reviseCoverLetter(CoverLetterReviseDto dto, Principal principal) throws JsonProcessingException;
    ChatResponseArray rewritteExperience(ReWritterExperienceDto dto, Principal principal) throws JsonProcessingException;

}
