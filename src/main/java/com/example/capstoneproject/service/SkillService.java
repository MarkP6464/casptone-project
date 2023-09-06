package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.ProjectViewDto;
import com.example.capstoneproject.Dto.SkillDto;
import com.example.capstoneproject.Dto.SkillViewDto;
import com.example.capstoneproject.Dto.SourceWorkViewDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SkillService extends BaseService<SkillDto, Integer> {
    boolean updateSkill(Integer id, SkillViewDto dto);
    List<SkillViewDto> getAllSkill(int cvId);
}
