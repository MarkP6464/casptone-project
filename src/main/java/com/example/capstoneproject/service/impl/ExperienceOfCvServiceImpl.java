package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.EducationViewDto;
import com.example.capstoneproject.Dto.ExperienceOfCvDto;
import com.example.capstoneproject.Dto.ExperienceViewDto;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.CvStatus;
import com.example.capstoneproject.mapper.EducationOfCvMapper;
import com.example.capstoneproject.mapper.ExperienceOfCvMapper;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.ExperienceOfCvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExperienceOfCvServiceImpl extends AbstractBaseService<ExperienceOfCv, ExperienceOfCvDto, Integer> implements ExperienceOfCvService {
    @Autowired
    ExperienceOfCvRepository experienceOfCvRepository;

    @Autowired
    ExperienceOfCvMapper experienceOfCvMapper;

    @Autowired
    CvRepository cvRepository;

    @Autowired
    ExperienceRepository experienceRepository;

    public ExperienceOfCvServiceImpl(ExperienceOfCvRepository experienceOfCvRepository, ExperienceOfCvMapper experienceOfCvMapper) {
        super(experienceOfCvRepository, experienceOfCvMapper, experienceOfCvRepository::findById);
        this.experienceOfCvRepository = experienceOfCvRepository;
        this.experienceOfCvMapper = experienceOfCvMapper;
    }

    @Override
    public boolean createExperienceOfCv(Integer cvId, Integer experienceId) {
        Experience experience = experienceRepository.findExperienceById(experienceId, CvStatus.ACTIVE);
        Cv cv = cvRepository.findCvById(cvId,CvStatus.ACTIVE);
        if (experience != null && cv != null) {
            ExperienceOfCv experienceOfCv = new ExperienceOfCv();
            experienceOfCv.setExperience(experience);
            experienceOfCv.setCv(cv);
            experienceOfCvRepository.save(experienceOfCv);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteExperienceOfCv(Integer cvId, Integer experienceId) {
        ExperienceOfCv experienceOfCv = experienceOfCvRepository.findByExperience_IdAndCv_Id(experienceId, cvId);

        if (experienceOfCv != null) {
            experienceOfCvRepository.delete(experienceOfCv);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<ExperienceViewDto> getActiveExperiencesByCvId(Integer cvId) {
        List<ExperienceOfCv> experienceOfCvs = experienceOfCvRepository.findActiveExperiencesByCvId(cvId, CvStatus.ACTIVE);
        List<ExperienceViewDto> experienceViewDtos = new ArrayList<>();

        for (ExperienceOfCv experienceOfCv : experienceOfCvs) {
            Experience experience = experienceOfCv.getExperience();
            ExperienceViewDto experienceViewDto = new ExperienceViewDto();
            experienceViewDto.setId(experience.getId());
            experienceViewDto.setRole(experience.getRole());
            experienceViewDto.setCompanyName(experience.getCompanyName());
            experienceViewDto.setStartDate(experience.getStartDate());
            experienceViewDto.setEndDate(experience.getEndDate());
            experienceViewDto.setLocation(experience.getLocation());
            experienceViewDto.setDescription(experience.getDescription());
            experienceViewDtos.add(experienceViewDto);
        }

        return experienceViewDtos;
    }

    @Override
    public List<ExperienceViewDto> getAllExperience(int cvId) {
        List<Experience> existingExperiences = experienceOfCvRepository.findExperiencesByCvId(cvId);
        List<Experience> activeExperiences = experienceRepository.findByStatus(CvStatus.ACTIVE);
        List<Experience> unassignedExperiences = activeExperiences.stream()
                .filter(experience -> !existingExperiences.contains(experience))
                .collect(Collectors.toList());

        return unassignedExperiences.stream()
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
}
