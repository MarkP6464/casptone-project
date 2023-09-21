package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.mapper.EducationMapper;
import com.example.capstoneproject.repository.EducationRepository;
import com.example.capstoneproject.service.UsersService;
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
    UsersService usersService;

    public EducationServiceImpl(EducationRepository educationRepository, EducationMapper educationMapper) {
        super(educationRepository, educationMapper, educationRepository::findById);
        this.educationRepository = educationRepository;
        this.educationMapper = educationMapper;
    }

    @Override
    public EducationDto createEducation(Integer id, EducationDto dto) {
        Education education = educationMapper.mapDtoToEntity(dto);
        Users Users = usersService.getUsersById(id);
        education.setUser(Users);
        education.setStatus(BasicStatus.ACTIVE);
        Education saved = educationRepository.save(education);
        return educationMapper.mapEntityToDto(saved);
    }

    @Override
    public boolean updateEducation(int UsersId, int educationId, EducationDto dto) {
        Optional<Education> existingEducationOptional = educationRepository.findById(educationId);
        if (existingEducationOptional.isPresent()) {
            Education existingEducation = existingEducationOptional.get();
            if (existingEducation.getUser().getId() != UsersId) {
                throw new IllegalArgumentException("Education does not belong to Users with id " + UsersId);
            }
            if (dto.getDegree() != null && !existingEducation.getDegree().equals(dto.getDegree())) {
                existingEducation.setDegree(dto.getDegree());
            } else {
                existingEducation.setDegree(existingEducation.getDegree());
            }
            if (dto.getCollegeName() != null && !existingEducation.getCollegeName().equals(dto.getCollegeName())) {
                existingEducation.setCollegeName(dto.getCollegeName());
            } else {
                existingEducation.setCollegeName(existingEducation.getCollegeName());
            }
            if (dto.getLocation() != null && !existingEducation.getLocation().equals(dto.getDegree())) {
                existingEducation.setLocation(dto.getLocation());
            } else {
                existingEducation.setLocation(existingEducation.getLocation());
            }
            if (dto.getEndYear() > 1950 && existingEducation.getEndYear() != dto.getEndYear()) {
                existingEducation.setEndYear(dto.getEndYear());
            } else {
                existingEducation.setEndYear(existingEducation.getEndYear());
            }
            if (dto.getMinor() != null && !existingEducation.getMinor().equals(dto.getMinor())) {
                existingEducation.setMinor(dto.getMinor());
            } else {
                existingEducation.setMinor(existingEducation.getMinor());
            }
            if (dto.getGpa() > 1 && existingEducation.getGpa()!=dto.getGpa()) {
                existingEducation.setGpa(dto.getGpa());
            } else {
                existingEducation.setGpa(existingEducation.getGpa());
            }
            if (dto.getDescription() != null && !existingEducation.getDescription().equals(dto.getDescription())) {
                existingEducation.setDescription(dto.getDescription());
            } else {
                existingEducation.setDescription(existingEducation.getDescription());
            }
            existingEducation.setStatus(BasicStatus.ACTIVE);
            Education updated = educationRepository.save(existingEducation);
            return true;
        } else {
            throw new IllegalArgumentException("Certification ID not found");
        }
    }

    @Override
    public List<EducationViewDto> getAllEducation(int UsersId) {
        List<Education> educations = educationRepository.findEducationsByStatus(UsersId, BasicStatus.ACTIVE);
        return educations.stream()
                .filter(education -> education.getStatus() == BasicStatus.ACTIVE)
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
    public void deleteEducationById(Integer UsersId,Integer educationId) {
        boolean isEducationBelongsToCv = educationRepository.existsByIdAndUser_Id(educationId, UsersId);

        if (isEducationBelongsToCv) {
            Optional<Education> Optional = educationRepository.findById(educationId);
            if (Optional.isPresent()) {
                Education education = Optional.get();
                education.setStatus(BasicStatus.DELETED);
                educationRepository.save(education);
            }
        } else {
            throw new IllegalArgumentException("Education with ID " + educationId + " does not belong to Users with ID " + UsersId);
        }
    }

}
