package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.CertificationDto;
import com.example.capstoneproject.Dto.EducationDto;
import com.example.capstoneproject.entity.Certification;
import com.example.capstoneproject.entity.Education;
import com.example.capstoneproject.enums.CvStatus;
import com.example.capstoneproject.mapper.CertificationMapper;
import com.example.capstoneproject.mapper.EducationMapper;
import com.example.capstoneproject.repository.CertificationRepository;
import com.example.capstoneproject.repository.EducationRepository;
import com.example.capstoneproject.service.CertificationService;
import com.example.capstoneproject.service.EducationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class EducationServiceImpl extends AbstractBaseService<Education, EducationDto, Integer> implements EducationService {
    @Autowired
    EducationRepository educationRepository;

    @Autowired
    EducationMapper educationMapper;

    public EducationServiceImpl(EducationRepository educationRepository, EducationMapper educationMapper) {
        super(educationRepository, educationMapper, educationRepository::findById);
        this.educationRepository = educationRepository;
        this.educationMapper = educationMapper;
    }

    @Override
    public List<EducationDto> getAll() {
        List<Education> educations = educationRepository.findEducationsByStatus(CvStatus.ACTIVE);
        return educationMapper.mapEntitiesToDtoes(educations);
    }

    @Override
    public EducationDto create(EducationDto dto) {
        Education education = educationMapper.mapDtoToEntity(dto);
        education.setStatus(CvStatus.ACTIVE);
        Education saved = educationRepository.save(education);
        return educationMapper.mapEntityToDto(saved);
    }

    @Override
    public EducationDto update(Integer id, EducationDto dto) {
        Optional<Education> existingEducationOptional = educationRepository.findById(id);
        if (existingEducationOptional.isPresent()) {
            Education existingEducation = existingEducationOptional.get();
            if (dto.getDegree() != null && !existingEducation.getDegree().equals(dto.getDegree())) {
                existingEducation.setDegree(dto.getDegree());
            } else {
                throw new IllegalArgumentException("New Degree is the same as the existing education");
            }
            if (dto.getNameCollege() != null && !existingEducation.getNameCollege().equals(dto.getNameCollege())) {
                existingEducation.setNameCollege(dto.getNameCollege());
            } else {
                throw new IllegalArgumentException("New Name College is the same as the existing education");
            }
            if (dto.getLocation() != null && !existingEducation.getLocation().equals(dto.getDegree())) {
                existingEducation.setLocation(dto.getLocation());
            } else {
                throw new IllegalArgumentException("New Location is the same as the existing education");
            }
            if (dto.getEndDate() != null && !existingEducation.getEndDate().equals(dto.getEndDate())) {
                existingEducation.setEndDate(dto.getEndDate());
            } else {
                throw new IllegalArgumentException("New End Date is the same as the existing education");
            }
            if (dto.getMinor() != null && !existingEducation.getMinor().equals(dto.getMinor())) {
                existingEducation.setMinor(dto.getMinor());
            } else {
                throw new IllegalArgumentException("New Minor is the same as the existing education");
            }
            if (dto.getGpa() > 0 && existingEducation.getGpa()!=dto.getGpa()) {
                existingEducation.setGpa(dto.getGpa());
            } else {
                throw new IllegalArgumentException("New GPA relevance is the same as the existing education");
            }
            if (dto.getDescription() != null && !existingEducation.getDescription().equals(dto.getDescription())) {
                existingEducation.setDescription(dto.getDescription());
            } else {
                throw new IllegalArgumentException("New Description is the same as the existing education");
            }
            existingEducation.setStatus(CvStatus.ACTIVE);
            Education updated = educationRepository.save(existingEducation);
            return educationMapper.mapEntityToDto(updated);
        } else {
            throw new IllegalArgumentException("Certification ID not found");
        }
    }

    @Override
    public void deleteById(Integer id) {
        Optional<Education> Optional = educationRepository.findById(id);
        if (Optional.isPresent()) {
            Education education = Optional.get();
            education.setStatus(CvStatus.DELETED);
            educationRepository.save(education);
        }
    }

}
