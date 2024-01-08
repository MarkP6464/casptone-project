package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.CvBodyDto;
import com.example.capstoneproject.Dto.EducationDto;
import com.example.capstoneproject.Dto.SkillDto;
import com.example.capstoneproject.Dto.responses.SkillViewDto;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.Skill;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.exception.ResourceNotFoundException;
import com.example.capstoneproject.mapper.SkillMapper;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.repository.SkillRepository;
import com.example.capstoneproject.service.CvService;
import com.example.capstoneproject.service.SkillService;
import com.example.capstoneproject.service.UsersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SkillServiceImpl extends AbstractBaseService<Skill, SkillDto, Integer> implements SkillService {
    @Autowired
    SkillRepository skillRepository;

    @Autowired
    SkillMapper skillMapper;

    @Autowired
    UsersService usersService;

    @Autowired
    CvService cvService;

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    CvRepository cvRepository;

    public SkillServiceImpl(SkillRepository skillRepository, SkillMapper skillMapper) {
        super(skillRepository, skillMapper, skillRepository::findById);
        this.skillRepository = skillRepository;
        this.skillMapper = skillMapper;
    }

    @Override
    public SkillDto createSkill(Integer id, SkillDto dto) {
        Skill skill = skillMapper.mapDtoToEntity(dto);
        Cv entity = cvRepository.getById(id);
        skill.setCv(entity);
        skill.setStatus(BasicStatus.ACTIVE);
        Skill saved = skillRepository.save(skill);
        return skillMapper.mapEntityToDto(saved);
    }


    @Override
    public boolean updateSkill(int UsersId, int skillId, SkillDto dto) {
        Optional<Skill> existingSkillOptional = skillRepository.findById(skillId);
        if (existingSkillOptional.isPresent()) {
            Skill existingSkill = existingSkillOptional.get();
            if (existingSkill.getCv().getId() != UsersId) {
                throw new IllegalArgumentException("Skill does not belong to Users with id " + UsersId);
            }
            if (dto.getDescription() != null && !existingSkill.getDescription().equals(dto.getDescription())) {
                existingSkill.setDescription(dto.getDescription());
            } else {
                existingSkill.setDescription(existingSkill.getDescription());
            }

            existingSkill.setStatus(BasicStatus.ACTIVE);
            Skill updated = skillRepository.save(existingSkill);
            return true;

        } else {
            throw new IllegalArgumentException("SKill ID not found");
        }
    }

    @Override
    public List<SkillViewDto> getAllSkill(int UsersId) {
        List<Skill> skills = skillRepository.findSkillsByStatus(UsersId, BasicStatus.ACTIVE);
        return skills.stream()
                .filter(skill -> skill.getStatus() == BasicStatus.ACTIVE)
                .map(skill -> {
                    SkillViewDto skillViewDto = new SkillViewDto();
                    skillViewDto.setId(skill.getId());
                    skillViewDto.setDescription(skill.getDescription());
                    return skillViewDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSkillById(Integer UsersId, Integer skillId) {
        boolean isSkillBelongsToCv = skillRepository.existsByIdAndCv_Id(skillId, UsersId);

        if (isSkillBelongsToCv) {
            Optional<Skill> Optional = skillRepository.findById(skillId);
            if (Optional.isPresent()) {
                Skill skill = Optional.get();
                skill.setStatus(BasicStatus.DELETED);
                skillRepository.save(skill);
            }
        } else {
            throw new IllegalArgumentException("Skill with ID " + skillId + " does not belong to Users with ID " + UsersId);
        }
    }


    @Override
    public SkillDto getAndIsDisplay(int cvId, int id) throws JsonProcessingException {
        Skill education = skillRepository.getById(id);
        if (Objects.nonNull(education)) {
            Cv cv = cvService.getCvById(cvId);
            CvBodyDto cvBodyDto = cv.deserialize();
            Optional<SkillDto> dto = cvBodyDto.getSkills().stream().filter(x -> x.getId() == id).findFirst();
            if (dto.isPresent()) {
                modelMapper.map(education, dto.get());
                return dto.get();
            } else {
                throw new ResourceNotFoundException("Not found that id in cvBody");
            }
        } else {
            throw new ResourceNotFoundException("Not found that id in cvBody");
        }
    }

    @Override
    public SkillDto getByIdInCvBody(int cvId, int id) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        Optional<SkillDto> dto = cvBodyDto.getSkills().stream().filter(x -> x.getId() == id).findFirst();
        if (dto.isPresent()) {
            return dto.get();
        } else {
            throw new ResourceNotFoundException("Not found that id in cvBody");
        }
    }

    @Override
    public List<SkillDto> getAllARelationInCvBody(int cvId) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        List<SkillDto> list = cvBodyDto.getSkills().stream().filter(x -> x.getStatus().equals(BasicStatus.ACTIVE)).collect(Collectors.toList());
        return list;
    }

    @Override
    public boolean updateInCvBody(int cvId, int id, SkillDto dto) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        Optional<SkillDto> relationDto = cvBodyDto.getSkills().stream().filter(x -> x.getId() == id).findFirst();
        if (relationDto.isPresent()) {
            Skill education = skillRepository.getById(id);
            modelMapper.map(dto, education);
            skillRepository.save(education);
            SkillDto educationDto = relationDto.get();
            modelMapper.map(dto, educationDto);
            educationDto.setTheOrder(dto.getTheOrder());
            cvService.updateCvBody(cvId, cvBodyDto);
            return true;
        } else {
            throw new IllegalArgumentException("education ID not found in cvBody");
        }
    }


    @Override
    public SkillDto createOfUserInCvBody(int cvId, SkillDto dto) throws JsonProcessingException {
        Skill education = skillMapper.mapDtoToEntity(dto);
        Cv cv = cvRepository.findCvById(cvId, BasicStatus.ACTIVE);
        if (Objects.isNull(cv)){
            throw new BadRequestException("Can not find the cv");
        }
        education.setCv(cv);
        education.setStatus(BasicStatus.ACTIVE);
        Skill saved = skillRepository.save(education);
        dto.setId(saved.getId());
        dto.setStatus(BasicStatus.ACTIVE);
        CvBodyDto cvBodyDto = cv.deserialize();
        Integer activeEdus = cvBodyDto.getSkills().stream()
                .filter(x->Objects.nonNull(x.getIsDisplay()) && x.getIsDisplay().equals(true)).collect(Collectors.toList()).size();
        dto.setTheOrder(activeEdus + 1);
        cvBodyDto.getSkills().add(dto);
        cvService.updateCvBody(cv.getId(), cvBodyDto);
        return skillMapper.mapEntityToDto(saved);
    }

    @Override
    public void deleteInCvBody(Integer cvId, Integer id) throws JsonProcessingException {
        Optional<Skill> Optional = skillRepository.findById(id);
        if (Optional.isPresent()) {
            Skill education = Optional.get();
            education.setStatus(BasicStatus.DELETED);
            skillRepository.save(education);
            try {
                CvBodyDto cvBodyDto = cvService.getCvBody(cvId);
                SkillDto dto = cvBodyDto.getSkills().stream().filter(e-> e.getId().equals(id)).findFirst().get();
                cvBodyDto.getSkills().forEach(c -> {
                    if (Objects.nonNull(c.getTheOrder()) && c.getTheOrder() > dto.getTheOrder()){
                        c.setTheOrder(c.getTheOrder() - 1);
                    }
                });
                dto.setIsDisplay(false);
                dto.setTheOrder(null);
                dto.setStatus(BasicStatus.DELETED);
                cvService.updateCvBody(cvId, cvBodyDto);

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
