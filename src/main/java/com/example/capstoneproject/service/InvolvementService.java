package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.ExperienceDto;
import com.example.capstoneproject.Dto.InvolvementDto;
import org.springframework.stereotype.Service;

@Service
public interface InvolvementService extends BaseService<InvolvementDto, Integer> {
    InvolvementDto update(Integer id, InvolvementDto dto);
}
