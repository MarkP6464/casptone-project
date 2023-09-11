package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.EducationDto;
import com.example.capstoneproject.Dto.SkillOfCvDto;
import com.example.capstoneproject.Dto.SkillViewDto;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.CvStatus;
import com.example.capstoneproject.mapper.SkillOfCvMapper;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.repository.SkillOfCvRepository;
import com.example.capstoneproject.repository.SkillRepository;
import com.example.capstoneproject.service.SkillOfCvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillOfCvServiceImpl extends AbstractBaseService<SkillOfCv, SkillOfCvDto, Integer> implements SkillOfCvService {
    @Autowired
    SkillOfCvRepository skillOfCvRepository;

    @Autowired
    SkillOfCvMapper skillOfCvMapper;

    @Autowired
    CvRepository cvRepository;

    @Autowired
    SkillRepository skillRepository;

    public SkillOfCvServiceImpl(SkillOfCvRepository skillOfCvRepository, SkillOfCvMapper skillOfCvMapper) {
        super(skillOfCvRepository, skillOfCvMapper, skillOfCvRepository::findById);
        this.skillOfCvRepository = skillOfCvRepository;
        this.skillOfCvMapper = skillOfCvMapper;
    }

    @Override
    public boolean createSkillOfCv(Integer cvId, Integer skillId) {
        Skill skill = skillRepository.findSkillById(skillId,CvStatus.ACTIVE);
        Cv cv = cvRepository.findCvById(cvId,CvStatus.ACTIVE);
        if (skill != null && cv != null) {
            SkillOfCv skillOfCv = new SkillOfCv();
            skillOfCv.setSkill(skill);
            skillOfCv.setCv(cv);
            skillOfCvRepository.save(skillOfCv);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteSkillOfCv(Integer cvId, Integer skillId) {
        SkillOfCv skillOfCv = skillOfCvRepository.findBySkill_IdAndCv_Id(skillId, cvId);

        if (skillOfCv != null) {
            skillOfCvRepository.delete(skillOfCv);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<SkillViewDto> getActiveSkillsByCvId(Integer cvId) {
        List<SkillOfCv> skillOfCvs = skillOfCvRepository.findActiveSkillsByCvId(cvId, CvStatus.ACTIVE);
        List<SkillViewDto> skillViewDtos = new ArrayList<>();

        for (SkillOfCv skillOfCv : skillOfCvs) {
            Skill skill = skillOfCv.getSkill();
            SkillViewDto skillViewDto = new SkillViewDto();
            skillViewDto.setId(skill.getId());
            skillViewDto.setDescription(skill.getDescription());
            skillViewDtos.add(skillViewDto);
        }

        return skillViewDtos;
    }

    //list not yet in skillofcv
    @Override
    public List<SkillViewDto> getAllSkill(int cvId) {
        List<Skill> existingSkills = skillOfCvRepository.findSkillsByCvId(cvId);
        List<Skill> activeSkills = skillRepository.findByStatus(CvStatus.ACTIVE);
        List<Skill> unassignedSkills = activeSkills.stream()
                .filter(skill -> !existingSkills.contains(skill))
                .collect(Collectors.toList());

        return unassignedSkills.stream()
                .map(skill -> {
                    SkillViewDto skillViewDto = new SkillViewDto();
                    skillViewDto.setId(skill.getId());
                    skillViewDto.setDescription(skill.getDescription());
                    return skillViewDto;
                })
                .collect(Collectors.toList());
    }
}
