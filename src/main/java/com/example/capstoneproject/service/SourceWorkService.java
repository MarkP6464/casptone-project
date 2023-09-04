package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.SkillDto;
import com.example.capstoneproject.Dto.SourceWorkDto;
import org.springframework.stereotype.Service;

@Service
public interface SourceWorkService extends BaseService<SourceWorkDto, Integer> {
    SourceWorkDto update(Integer id, SourceWorkDto dto);
}
