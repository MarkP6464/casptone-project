package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.EducationDto;
import com.example.capstoneproject.Dto.ExperienceDto;
import com.example.capstoneproject.entity.Education;
import com.example.capstoneproject.entity.Experience;
import com.example.capstoneproject.enums.CvStatus;
import com.example.capstoneproject.mapper.EducationMapper;
import com.example.capstoneproject.mapper.ExperienceMapper;
import com.example.capstoneproject.repository.EducationRepository;
import com.example.capstoneproject.repository.ExperienceRepository;
import com.example.capstoneproject.service.EducationService;
import com.example.capstoneproject.service.ExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExperienceServiceImpl extends AbstractBaseService<Experience, ExperienceDto, Integer> implements ExperienceService {
    @Autowired
    ExperienceRepository experienceRepository;

    @Autowired
    ExperienceMapper experienceMapper;

    public ExperienceServiceImpl(ExperienceRepository experienceRepository, ExperienceMapper experienceMapper) {
        super(experienceRepository, experienceMapper, experienceRepository::findById);
        this.experienceRepository = experienceRepository;
        this.experienceMapper = experienceMapper;
    }

    @Override
    public List<ExperienceDto> getAll() {
        List<Experience> experiences = experienceRepository.findExperiencesByStatus(CvStatus.ACTIVE);
        return experienceMapper.mapEntitiesToDtoes(experiences);
    }

    @Override
    public ExperienceDto create(ExperienceDto dto) {
        Experience experience = experienceMapper.mapDtoToEntity(dto);
        experience.setStatus(CvStatus.ACTIVE);
        Experience saved = experienceRepository.save(experience);
        return experienceMapper.mapEntityToDto(saved);
    }

    @Override
    public ExperienceDto update(Integer id, ExperienceDto dto) {
        Optional<Experience> existingExperienceOptional = experienceRepository.findById(id);
        if (existingExperienceOptional.isPresent()) {
            Experience existingExperience = existingExperienceOptional.get();
            if (dto.getRoleCompany() != null && !existingExperience.getRoleCompany().equals(dto.getRoleCompany())) {
                existingExperience.setRoleCompany(dto.getRoleCompany());
            } else {
                throw new IllegalArgumentException("New Role Company is the same as the existing experience");
            }
            if (dto.getName() != null && !existingExperience.getName().equals(dto.getName())) {
                existingExperience.setName(dto.getName());
            } else {
                throw new IllegalArgumentException("New Name is the same as the existing experience");
            }
            if (dto.getStartDate() != null && !existingExperience.getStartDate().equals(dto.getStartDate())) {
                existingExperience.setStartDate(dto.getStartDate());
            } else {
                throw new IllegalArgumentException("New Start Date is the same as the existing experience");
            }
            if (dto.getEndDate() != null && !existingExperience.getEndDate().equals(dto.getEndDate())) {
                existingExperience.setEndDate(dto.getEndDate());
            } else {
                throw new IllegalArgumentException("New End Date is the same as the existing experience");
            }
            if (dto.getLocation() != null && !existingExperience.getLocation().equals(dto.getLocation())) {
                existingExperience.setLocation(dto.getLocation());
            } else {
                throw new IllegalArgumentException("New Location is the same as the existing experience");
            }
            if (dto.getDescription() != null && !existingExperience.getDescription().equals(dto.getDescription())) {
                existingExperience.setDescription(dto.getDescription());
            } else {
                throw new IllegalArgumentException("New Description is the same as the existing experience");
            }

            existingExperience.setStatus(CvStatus.ACTIVE);
            Experience updated = experienceRepository.save(existingExperience);
            return experienceMapper.mapEntityToDto(updated);
        } else {
            throw new IllegalArgumentException("Experience ID not found");
        }
    }

    @Override
    public void deleteById(Integer id) {
        Optional<Experience> Optional = experienceRepository.findById(id);
        if (Optional.isPresent()) {
            Experience experience = Optional.get();
            experience.setStatus(CvStatus.DELETED);
            experienceRepository.save(experience);
        }
    }

}
