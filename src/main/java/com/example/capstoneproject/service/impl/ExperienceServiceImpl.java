package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.CvStatus;
import com.example.capstoneproject.mapper.EducationMapper;
import com.example.capstoneproject.mapper.ExperienceMapper;
import com.example.capstoneproject.repository.EducationRepository;
import com.example.capstoneproject.repository.ExperienceRepository;
import com.example.capstoneproject.service.CvService;
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

    @Autowired
    CvService cvService;

    public ExperienceServiceImpl(ExperienceRepository experienceRepository, ExperienceMapper experienceMapper) {
        super(experienceRepository, experienceMapper, experienceRepository::findById);
        this.experienceRepository = experienceRepository;
        this.experienceMapper = experienceMapper;
    }

    @Override
    public ExperienceDto createExperience(Integer id, ExperienceDto dto) {
        Experience experience = experienceMapper.mapDtoToEntity(dto);
        Cv cv = cvService.getCvById(id);
        experience.setCv(cv);
        experience.setStatus(CvStatus.ACTIVE);
        Experience saved = experienceRepository.save(experience);
        return experienceMapper.mapEntityToDto(saved);
    }

    @Override
    public boolean updateExperience(int cvId, int experienceId, ExperienceDto dto) {
        Optional<Experience> existingExperienceOptional = experienceRepository.findById(experienceId);
        if (existingExperienceOptional.isPresent()) {
            Experience existingExperience = existingExperienceOptional.get();
            if (existingExperience.getCv().getId() != cvId) {
                throw new IllegalArgumentException("Experience does not belong to CV with id " + cvId);
            }
            if (dto.getRole() != null && !existingExperience.getRole().equals(dto.getRole())) {
                existingExperience.setRole(dto.getRole());
            } else {
                throw new IllegalArgumentException("New Role Company is the same as the existing experience");
            }
            if (dto.getCompanyName() != null && !existingExperience.getCompanyName().equals(dto.getCompanyName())) {
                existingExperience.setCompanyName(dto.getCompanyName());
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
    public void deleteExperienceById(Integer cvId,Integer experienceId) {
        boolean isExperienceBelongsToCv = experienceRepository.existsByIdAndCv_Id(experienceId, cvId);

        if (isExperienceBelongsToCv) {
            Optional<Experience> Optional = experienceRepository.findById(experienceId);
            if (Optional.isPresent()) {
                Experience experience = Optional.get();
                experience.setStatus(CvStatus.DELETED);
                experienceRepository.save(experience);
            }
        } else {
            throw new IllegalArgumentException("Experience with ID " + experienceId + " does not belong to CV with ID " + cvId);
        }
    }

}
