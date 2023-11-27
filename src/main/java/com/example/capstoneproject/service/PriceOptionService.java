package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.PriceOptionDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PriceOptionService {
    String createPriceOption(Integer expert, PriceOptionDto dto);

    String updatePriceOption(Integer expertId, Integer optionId, PriceOptionDto dto);

    void editPriceOption(Integer expertId, List<PriceOptionDto> dto);
}
