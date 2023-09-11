package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.SkillOfCvDto;
import com.example.capstoneproject.Dto.SkillViewDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SkillOfCvService extends BaseService<SkillOfCvDto, Integer> {
    boolean createSkillOfCv(Integer cvId, Integer skillId);
    boolean deleteSkillOfCv(Integer cvId, Integer skillId);
    List<SkillViewDto> getActiveSkillsByCvId(Integer cvId);
    List<SkillViewDto> getAllSkill(int cvId);
}
