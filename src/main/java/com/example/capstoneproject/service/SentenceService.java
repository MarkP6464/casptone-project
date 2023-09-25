package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.AtsDto;
import com.example.capstoneproject.Dto.ChatRequest;
import com.example.capstoneproject.Dto.ResultDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SentenceService {
    ResultDto checkSentences(String sentences);
    List<AtsDto> ListAts(ChatRequest chatRequest);

//    String checkShortBulletPoint(String text);
//    String checkPunctuatedBulletPoint(String text);
//    String checkQuantifiedBulletPoint(String text);
//    String checkIncorrectNumberBulletPoint(String text);
//    String checkPersonalPronounsBulletPoint(String text);
//    String checkFillerBulletPoint(String text);
}
