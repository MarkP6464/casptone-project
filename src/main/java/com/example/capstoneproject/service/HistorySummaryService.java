package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.HistorySummaryDto;
import com.example.capstoneproject.Dto.responses.HistorySummaryViewDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HistorySummaryService {
    void createHistorySummary(Integer cvId, String summary);

    List<HistorySummaryDto> getHistorySummaries(Integer cvId);

    HistorySummaryViewDto getHistorySummary(Integer summaryId);

}
