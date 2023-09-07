package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.Project;
import com.example.capstoneproject.entity.Skill;
import com.example.capstoneproject.enums.CvStatus;
import com.example.capstoneproject.mapper.ProjectMapper;
import com.example.capstoneproject.mapper.SkillMapper;
import com.example.capstoneproject.repository.ProjectRepository;
import com.example.capstoneproject.repository.SkillRepository;
import com.example.capstoneproject.service.ProjectService;
import com.example.capstoneproject.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SkillServiceImpl extends AbstractBaseService<Skill, SkillDto, Integer> implements SkillService {
    @Autowired
    SkillRepository skillRepository;

    @Autowired
    SkillMapper skillMapper;

    public SkillServiceImpl(SkillRepository skillRepository, SkillMapper skillMapper) {
        super(skillRepository, skillMapper, skillRepository::findById);
        this.skillRepository = skillRepository;
        this.skillMapper = skillMapper;
    }


    @Override
    public boolean updateSkill(Integer id, SkillViewDto dto) {
        Optional<Skill> existingSkillOptional = skillRepository.findById(id);
        if (existingSkillOptional.isPresent()) {
            Skill existingSkill = existingSkillOptional.get();
            if (dto.getName() != null && !existingSkill.getName().equals(dto.getName())) {
                existingSkill.setName(dto.getName());
            } else {
                throw new IllegalArgumentException("New Name is the same as the existing skill");
            }

            existingSkill.setStatus(CvStatus.ACTIVE);
            Skill updated = skillRepository.save(existingSkill);
            return true;

        } else {
            throw new IllegalArgumentException("SKill ID not found");
        }
    }

    @Override
    public List<SkillViewDto> getAllSkill(int cvId) {
        List<Skill> skills = skillRepository.findSkillsByStatus(cvId,CvStatus.ACTIVE);
        return skills.stream()
                .filter(skill -> skill.getStatus() == CvStatus.ACTIVE)
                .map(skill -> {
                    SkillViewDto skillViewDto = new SkillViewDto();
                    skillViewDto.setId(skill.getId());
                    skillViewDto.setName(skill.getName());
                    skillViewDto.setStatus(skill.getStatus());
                    return skillViewDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Integer id) {
        Optional<Skill> Optional = skillRepository.findById(id);
        if (Optional.isPresent()) {
            Skill skill = Optional.get();
            skill.setStatus(CvStatus.DELETED);
            skillRepository.save(skill);
        }
    }

}