package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.HistoryCoverLetterDto;
import com.example.capstoneproject.Dto.responses.AdminConfigurationResponse;
import org.springframework.stereotype.Service;

@Service
public interface HistoryCoverLetterService {
    HistoryCoverLetterDto get(Integer id);
}
