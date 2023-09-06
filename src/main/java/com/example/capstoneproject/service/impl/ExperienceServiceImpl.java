package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.EducationDto;
import com.example.capstoneproject.Dto.EducationViewDto;
import com.example.capstoneproject.Dto.ExperienceDto;
import com.example.capstoneproject.Dto.ExperienceViewDto;
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
import java.util.stream.Collectors;

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
    public boolean updateExperience(Integer id, ExperienceViewDto dto) {
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
            return true;
        } else {
            throw new IllegalArgumentException("Experience ID not found");
        }
    }

    @Override
    public List<ExperienceViewDto> getAllExperience(int cvId) {
        List<Experience> experiences = experienceRepository.findExperiencesByStatus(cvId,CvStatus.ACTIVE);
        return experiences.stream()
                .filter(experience -> experience.getStatus() == CvStatus.ACTIVE)
                .map(experience -> {
                    ExperienceViewDto experienceViewDto = new ExperienceViewDto();
                    experienceViewDto.setId(experience.getId());
                    experienceViewDto.setRoleCompany(experience.getRoleCompany());
                    experienceViewDto.setName(experience.getName());
                    experienceViewDto.setStartDate(experience.getStartDate());
                    experienceViewDto.setEndDate(experience.getEndDate());
                    experienceViewDto.setLocation(experience.getLocation());
                    experienceViewDto.setDescription(experience.getDescription());
                    experienceViewDto.setStatus(experience.getStatus());
                    return experienceViewDto;
                })
                .collect(Collectors.toList());
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
