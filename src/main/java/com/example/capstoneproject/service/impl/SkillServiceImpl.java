package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.CvStatus;
import com.example.capstoneproject.mapper.ProjectMapper;
import com.example.capstoneproject.mapper.SkillMapper;
import com.example.capstoneproject.repository.ProjectRepository;
import com.example.capstoneproject.repository.SkillOfCvRepository;
import com.example.capstoneproject.repository.SkillRepository;
import com.example.capstoneproject.service.CustomerService;
import com.example.capstoneproject.service.CvService;
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

    @Autowired
    CustomerService customerService;

    @Autowired
    SkillOfCvRepository skillOfCvRepository;

    public SkillServiceImpl(SkillRepository skillRepository, SkillMapper skillMapper) {
        super(skillRepository, skillMapper, skillRepository::findById);
        this.skillRepository = skillRepository;
        this.skillMapper = skillMapper;
    }

    @Override
    public SkillDto createSkill(Integer id, SkillDto dto) {
        Skill skill = skillMapper.mapDtoToEntity(dto);
        Customer customer = customerService.getCustomerById(id);
        skill.setCustomer(customer);
        skill.setStatus(CvStatus.ACTIVE);
        Skill saved = skillRepository.save(skill);
        return skillMapper.mapEntityToDto(saved);
    }


    @Override
    public boolean updateSkill(int customerId, int skillId, SkillDto dto) {
        Optional<Skill> existingSkillOptional = skillRepository.findById(skillId);
        if (existingSkillOptional.isPresent()) {
            Skill existingSkill = existingSkillOptional.get();
            if (existingSkill.getCustomer().getId() != customerId) {
                throw new IllegalArgumentException("Skill does not belong to Customer with id " + customerId);
            }
            if (dto.getDescription() != null && !existingSkill.getDescription().equals(dto.getDescription())) {
                existingSkill.setDescription(dto.getDescription());
            } else {
                existingSkill.setDescription(existingSkill.getDescription());
            }

            existingSkill.setStatus(CvStatus.ACTIVE);
            Skill updated = skillRepository.save(existingSkill);
            return true;

        } else {
            throw new IllegalArgumentException("SKill ID not found");
        }
    }

    @Override
    public List<SkillViewDto> getAllSkill(int customerId) {
        List<Skill> skills = skillRepository.findSkillsByStatus(customerId,CvStatus.ACTIVE);
        return skills.stream()
                .filter(skill -> skill.getStatus() == CvStatus.ACTIVE)
                .map(skill -> {
                    SkillViewDto skillViewDto = new SkillViewDto();
                    skillViewDto.setId(skill.getId());
                    skillViewDto.setDescription(skill.getDescription());
                    return skillViewDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSkillById(Integer customerId,Integer skillId) {
        boolean isSkillBelongsToCv = skillRepository.existsByIdAndCustomer_Id(skillId, customerId);

        if (isSkillBelongsToCv) {
            Optional<Skill> Optional = skillRepository.findById(skillId);
            if (Optional.isPresent()) {
                Skill skill = Optional.get();
                skill.setStatus(CvStatus.DELETED);
                skillRepository.save(skill);
            }
        } else {
            throw new IllegalArgumentException("Skill with ID " + skillId + " does not belong to Customer with ID " + customerId);
        }
    }

}
