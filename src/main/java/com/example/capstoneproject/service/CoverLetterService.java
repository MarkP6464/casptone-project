package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.ChatResponse;
import com.example.capstoneproject.Dto.CoverLetterAddDto;
import com.example.capstoneproject.Dto.CoverLetterDto;
import com.example.capstoneproject.Dto.CoverLetterUpdateDto;
import com.example.capstoneproject.Dto.responses.CoverLetterViewDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public interface CoverLetterService extends BaseService<CoverLetterDto, Integer>{
    CoverLetterViewDto createCoverLetter(Integer id, CoverLetterAddDto dto);
    boolean updateCoverLetter(int UsersId, int coverLetterId, CoverLetterUpdateDto dto);
    boolean deleteCoverLetterById(Integer UsersId,Integer coverLetterId);
    CoverLetterDto getCoverLetter(Integer userId, Integer coverLetterId);
    ChatResponse reviseCoverLetter(String content, String improve) throws JsonProcessingException;
}
