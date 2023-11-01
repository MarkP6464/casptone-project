package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.HistoryDto;
import com.example.capstoneproject.Dto.responses.HistoryViewDto;
import org.springframework.stereotype.Service;

@Service
public interface HistoryService {
    HistoryViewDto create(Integer userId, Integer cvId, HistoryDto dto);
}
