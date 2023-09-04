package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.SkillDto;
import org.springframework.stereotype.Service;

@Service
public interface SkillService extends BaseService<SkillDto, Integer> {
    SkillDto update(Integer id, SkillDto dto);
}
