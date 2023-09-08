package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.CvStatus;
import com.example.capstoneproject.mapper.CertificationMapper;
import com.example.capstoneproject.mapper.EducationMapper;
import com.example.capstoneproject.repository.CertificationRepository;
import com.example.capstoneproject.repository.EducationRepository;
import com.example.capstoneproject.service.CertificationService;
import com.example.capstoneproject.service.CvService;
import com.example.capstoneproject.service.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EducationServiceImpl extends AbstractBaseService<Education, EducationDto, Integer> implements EducationService {
    @Autowired
    EducationRepository educationRepository;

    @Autowired
    EducationMapper educationMapper;

    @Autowired
    CvService cvService;

    public EducationServiceImpl(EducationRepository educationRepository, EducationMapper educationMapper) {
        super(educationRepository, educationMapper, educationRepository::findById);
        this.educationRepository = educationRepository;
        this.educationMapper = educationMapper;
    }

    @Override
    public EducationDto createEducation(Integer id, EducationDto dto) {
        Education education = educationMapper.mapDtoToEntity(dto);
        Cv cv = cvService.getCvById(id);
        education.setCv(cv);
        education.setStatus(CvStatus.ACTIVE);
        Education saved = educationRepository.save(education);
        return educationMapper.mapEntityToDto(saved);
    }

    @Override
    public boolean updateEducation(int cvId, int educationId, EducationDto dto) {
        Optional<Education> existingEducationOptional = educationRepository.findById(educationId);
        if (existingEducationOptional.isPresent()) {
            Education existingEducation = existingEducationOptional.get();
            if (existingEducation.getCv().getId() != cvId) {
                throw new IllegalArgumentException("Education does not belong to CV with id " + cvId);
            }
            if (dto.getDegree() != null && !existingEducation.getDegree().equals(dto.getDegree())) {
                existingEducation.setDegree(dto.getDegree());
            } else {
                throw new IllegalArgumentException("New Degree is the same as the existing education");
            }
            if (dto.getCollegeName() != null && !existingEducation.getCollegeName().equals(dto.getCollegeName())) {
                existingEducation.setCollegeName(dto.getCollegeName());
            } else {
                throw new IllegalArgumentException("New Name College is the same as the existing education");
            }
            if (dto.getLocation() != null && !existingEducation.getLocation().equals(dto.getDegree())) {
                existingEducation.setLocation(dto.getLocation());
            } else {
                throw new IllegalArgumentException("New Location is the same as the existing education");
            }
            if (dto.getEndYear() > 1950 && existingEducation.getEndYear() != dto.getEndYear()) {
                existingEducation.setEndYear(dto.getEndYear());
            } else {
                throw new IllegalArgumentException("New End Date is the same as the existing education");
            }
            if (dto.getMinor() != null && !existingEducation.getMinor().equals(dto.getMinor())) {
                existingEducation.setMinor(dto.getMinor());
            } else {
                throw new IllegalArgumentException("New Minor is the same as the existing education");
            }
            if (dto.getGpa() > 1 && existingEducation.getGpa()!=dto.getGpa()) {
                existingEducation.setGpa(dto.getGpa());
            } else {
                throw new IllegalArgumentException("New GPA large than 1");
            }
            if (dto.getDescription() != null && !existingEducation.getDescription().equals(dto.getDescription())) {
                existingEducation.setDescription(dto.getDescription());
            } else {
                throw new IllegalArgumentException("New Description is the same as the existing education");
            }
            existingEducation.setStatus(CvStatus.ACTIVE);
            Education updated = educationRepository.save(existingEducation);
            return true;
        } else {
            throw new IllegalArgumentException("Certification ID not found");
        }
    }

    @Override
    public List<EducationViewDto> getAllEducation(int cvId) {
        List<Education> educations = educationRepository.findEducationsByStatus(cvId,CvStatus.ACTIVE);
        return educations.stream()
                .filter(education -> education.getStatus() == CvStatus.ACTIVE)
                .map(education -> {
                    EducationViewDto educationViewDto = new EducationViewDto();
                    educationViewDto.setId(education.getId());
                    educationViewDto.setDegree(education.getDegree());
                    educationViewDto.setCollegeName(education.getCollegeName());
                    educationViewDto.setLocation(education.getLocation());
                    educationViewDto.setEndYear(education.getEndYear());
                    educationViewDto.setMinor(education.getMinor());
                    educationViewDto.setGpa(education.getGpa());
                    educationViewDto.setDescription(education.getDescription());
                    return educationViewDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteEducationById(Integer cvId,Integer educationId) {
        boolean isEducationBelongsToCv = educationRepository.existsByIdAndCv_Id(educationId, cvId);

        if (isEducationBelongsToCv) {
            Optional<Education> Optional = educationRepository.findById(educationId);
            if (Optional.isPresent()) {
                Education education = Optional.get();
                education.setStatus(CvStatus.DELETED);
                educationRepository.save(education);
            }
        } else {
            throw new IllegalArgumentException("Education with ID " + educationId + " does not belong to CV with ID " + cvId);
        }
    }

}
