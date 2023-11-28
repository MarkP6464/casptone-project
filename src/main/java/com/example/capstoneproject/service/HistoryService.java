package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.HistoryDto;
import com.example.capstoneproject.Dto.responses.HistoryDateViewDto;
import com.example.capstoneproject.Dto.responses.HistoryViewDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HistoryService {
    HistoryViewDto create(Integer userId, Integer cvId) throws JsonProcessingException;

    List<HistoryDateViewDto> getListHistoryDate(Integer userId, Integer cvId);

    HistoryDto getHistory(Integer historyId) throws JsonProcessingException;

    HistoryDto getHistoryById(Integer historyId);
}
