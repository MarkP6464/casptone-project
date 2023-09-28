package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.SkillViewDto;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.exception.ResourceNotFoundException;
import com.example.capstoneproject.mapper.SkillMapper;
import com.example.capstoneproject.repository.SkillRepository;
import com.example.capstoneproject.service.CvService;
import com.example.capstoneproject.service.UsersService;
import com.example.capstoneproject.service.SkillService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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

    public SkillServiceImpl(SkillRepository skillRepository, SkillMapper skillMapper) {
        super(skillRepository, skillMapper, skillRepository::findById);
        this.skillRepository = skillRepository;
        this.skillMapper = skillMapper;
    }

    @Override
    public SkillDto createSkill(Integer id, SkillDto dto) {
        Skill skill = skillMapper.mapDtoToEntity(dto);
        Users Users = usersService.getUsersById(id);
        skill.setUser(Users);
        skill.setStatus(BasicStatus.ACTIVE);
        Skill saved = skillRepository.save(skill);
        return skillMapper.mapEntityToDto(saved);
    }


    @Override
    public boolean updateSkill(int UsersId, int skillId, SkillDto dto) {
        Optional<Skill> existingSkillOptional = skillRepository.findById(skillId);
        if (existingSkillOptional.isPresent()) {
            Skill existingSkill = existingSkillOptional.get();
            if (existingSkill.getUser().getId() != UsersId) {
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
    public void deleteSkillById(Integer UsersId,Integer skillId) {
        boolean isSkillBelongsToCv = skillRepository.existsByIdAndUser_Id(skillId, UsersId);

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
        if (Objects.nonNull(education)){
            Cv cv = cvService.getCvById(cvId);
            CvBodyDto cvBodyDto = cv.deserialize();
            Optional<SkillDto> dto = cvBodyDto.getSkills().stream().filter(x -> x.getId()==id).findFirst();
            if (dto.isPresent()){
                modelMapper.map(education, dto.get());
                return dto.get();
            }else{
                throw new ResourceNotFoundException("Not found that id in cvBody");
            }
        }else{
            throw new ResourceNotFoundException("Not found that id in cvBody");
        }
    }

    @Override
    public SkillDto getByIdInCvBody(int cvId, int id) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        Optional<SkillDto> dto = cvBodyDto.getSkills().stream().filter(x -> x.getId()==id).findFirst();
        if (dto.isPresent()){
            return  dto.get();
        }else{
            throw new ResourceNotFoundException("Not found that id in cvBody");
        }
    }

    @Override
    public Set<SkillDto> getAllARelationInCvBody(int cvId) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        return cvBodyDto.getSkills();
    }

    @Override
    public boolean updateInCvBody(int cvId, int id, SkillDto dto) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        Optional<SkillDto> relationDto = cvBodyDto.getSkills().stream().filter(x -> x.getId()==id).findFirst();
        if (relationDto.isPresent()) {
            Skill education = skillRepository.getById(id);
            modelMapper.map(dto, education);
            skillRepository.save(education);
            SkillDto educationDto = relationDto.get();
            educationDto.setIsDisplay(dto.getIsDisplay());
            cvService.updateCvBody(cvId, cvBodyDto);
            return true;
        } else {
            throw new IllegalArgumentException("education ID not found in cvBody");
        }
    }


    @Override
    public SkillDto createOfUserInCvBody(int cvId, SkillDto dto) throws JsonProcessingException {
        Skill education = skillMapper.mapDtoToEntity(dto);
        Users Users = usersService.getUsersById(cvService.getCvById(cvId).getUser().getId());
        education.setUser(Users);
        education.setStatus(BasicStatus.ACTIVE);
        Skill saved = skillRepository.save(education);
        SkillDto educationViewDto = new SkillDto();
        educationViewDto.setId(saved.getId());
        CvBodyDto cvBodyDto = cvService.getCvBody(cvId);
        cvBodyDto.getSkills().add(educationViewDto);
        educationViewDto.setIsDisplay(true);
        cvService.updateCvBody(cvId, cvBodyDto);
        return educationViewDto;
    }

    @Override
    public void deleteInCvBody(Integer cvId, Integer id) throws JsonProcessingException {

        Optional<Skill> Optional = skillRepository.findById(id);
        if (Optional.isPresent()) {
            Skill education = Optional.get();
            education.setStatus(BasicStatus.DELETED);
            skillRepository.save(education);
            CvBodyDto cvBodyDto = cvService.getCvBody(cvId);
            cvBodyDto.getEducations().removeIf(x -> x.getId() == id);
            cvService.updateCvBody(cvId, cvBodyDto);
        }
    }

}
