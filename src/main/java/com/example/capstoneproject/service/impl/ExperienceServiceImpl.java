package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.mapper.ExperienceMapper;
import com.example.capstoneproject.repository.ExperienceRepository;
import com.example.capstoneproject.service.UsersService;
import com.example.capstoneproject.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExperienceServiceImpl extends AbstractBaseService<Experience, ExperienceDto, Integer> implements ExperienceService {
    @Autowired
    ExperienceRepository experienceRepository;

    @Autowired
    ExperienceMapper experienceMapper;

    @Autowired
    UsersService UsersService;

    public ExperienceServiceImpl(ExperienceRepository experienceRepository, ExperienceMapper experienceMapper) {
        super(experienceRepository, experienceMapper, experienceRepository::findById);
        this.experienceRepository = experienceRepository;
        this.experienceMapper = experienceMapper;
    }

    @Override
    public ExperienceDto createExperience(Integer id, ExperienceDto dto) {
        Experience experience = experienceMapper.mapDtoToEntity(dto);
        Users Users = UsersService.getUsersById(id);
        experience.setUser(Users);
        experience.setStatus(BasicStatus.ACTIVE);
        Experience saved = experienceRepository.save(experience);
        return experienceMapper.mapEntityToDto(saved);
    }

    @Override
    public boolean updateExperience(int UsersId, int experienceId, ExperienceDto dto) {
        Optional<Experience> existingExperienceOptional = experienceRepository.findById(experienceId);
        if (existingExperienceOptional.isPresent()) {
            Experience existingExperience = existingExperienceOptional.get();
            if (existingExperience.getUser().getId() != UsersId) {
                throw new IllegalArgumentException("Experience does not belong to Users with id " + UsersId);
            }
            if (dto.getRole() != null && !existingExperience.getRole().equals(dto.getRole())) {
                existingExperience.setRole(dto.getRole());
            } else {
                existingExperience.setRole(existingExperience.getRole());
            }
            if (dto.getCompanyName() != null && !existingExperience.getCompanyName().equals(dto.getCompanyName())) {
                existingExperience.setCompanyName(dto.getCompanyName());
            } else {
                existingExperience.setCompanyName(existingExperience.getCompanyName());
            }
            if (dto.getStartDate() != null && !existingExperience.getStartDate().equals(dto.getStartDate())) {
                existingExperience.setStartDate(dto.getStartDate());
            } else {
                existingExperience.setStartDate(existingExperience.getStartDate());
            }
            if (dto.getEndDate() != null && !existingExperience.getEndDate().equals(dto.getEndDate())) {
                existingExperience.setEndDate(dto.getEndDate());
            } else {
                existingExperience.setEndDate(existingExperience.getEndDate());
            }
            if (dto.getLocation() != null && !existingExperience.getLocation().equals(dto.getLocation())) {
                existingExperience.setLocation(dto.getLocation());
            } else {
                existingExperience.setLocation(existingExperience.getLocation());
            }
            if (dto.getDescription() != null && !existingExperience.getDescription().equals(dto.getDescription())) {
                existingExperience.setDescription(dto.getDescription());
            } else {
                existingExperience.setDescription(existingExperience.getDescription());
            }

            existingExperience.setStatus(BasicStatus.ACTIVE);
            Experience updated = experienceRepository.save(existingExperience);
            return true;
        } else {
            throw new IllegalArgumentException("Experience ID not found");
        }
    }

    @Override
    public List<ExperienceViewDto> getAllExperience(int UsersId) {
        List<Experience> experiences = experienceRepository.findExperiencesByStatus(UsersId, BasicStatus.ACTIVE);
        return experiences.stream()
                .filter(experience -> experience.getStatus() == BasicStatus.ACTIVE)
                .map(experience -> {
                    ExperienceViewDto experienceViewDto = new ExperienceViewDto();
                    experienceViewDto.setId(experience.getId());
                    experienceViewDto.setRole(experience.getRole());
                    experienceViewDto.setCompanyName(experience.getCompanyName());
                    experienceViewDto.setStartDate(experience.getStartDate());
                    experienceViewDto.setEndDate(experience.getEndDate());
                    experienceViewDto.setLocation(experience.getLocation());
                    experienceViewDto.setDescription(experience.getDescription());
                    return experienceViewDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteExperienceById(Integer UsersId,Integer experienceId) {
        boolean isExperienceBelongsToCv = experienceRepository.existsByIdAndUser_Id(experienceId, UsersId);

        if (isExperienceBelongsToCv) {
            Optional<Experience> Optional = experienceRepository.findById(experienceId);
            if (Optional.isPresent()) {
                Experience experience = Optional.get();
                experience.setStatus(BasicStatus.DELETED);
                experienceRepository.save(experience);
            }
        } else {
            throw new IllegalArgumentException("Experience with ID " + experienceId + " does not belong to Users with ID " + UsersId);
        }
    }

}
