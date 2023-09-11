package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SkillService extends BaseService<SkillDto, Integer> {
    boolean updateSkill(int customerId, int skillId, SkillDto dto);
    List<SkillViewDto> getAllSkill(int customerId);
    SkillDto createSkill(Integer id, SkillDto dto);
    void deleteSkillById(Integer customerIdId,Integer skillId);
}
