package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.SkillDto;
import com.example.capstoneproject.Dto.responses.SkillViewDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SkillService extends BaseService<SkillDto, Integer> {
    boolean updateSkill(int UsersId, int skillId, SkillDto dto);

    List<SkillViewDto> getAllSkill(int UsersId);

    SkillDto createSkill(Integer id, SkillDto dto);

    void deleteSkillById(Integer UsersId, Integer skillId);

    SkillDto getAndIsDisplay(int cvId, int id) throws JsonProcessingException;

    SkillDto getByIdInCvBody(int cvId, int id) throws JsonProcessingException;

    List<SkillDto> getAllARelationInCvBody(int cvId) throws JsonProcessingException;

    boolean updateInCvBody(int cvId, int id, SkillDto dto) throws JsonProcessingException;

    SkillDto createOfUserInCvBody(int cvId, SkillDto dto) throws JsonProcessingException;

    void deleteInCvBody(Integer cvId, Integer id) throws JsonProcessingException;
}
