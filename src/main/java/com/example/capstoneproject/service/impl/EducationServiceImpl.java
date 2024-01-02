package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.CertificationDto;
import com.example.capstoneproject.Dto.CvBodyDto;
import com.example.capstoneproject.Dto.EducationDto;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.Education;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.exception.ResourceNotFoundException;
import com.example.capstoneproject.mapper.EducationMapper;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.repository.EducationRepository;
import com.example.capstoneproject.service.CvService;
import com.example.capstoneproject.service.EducationService;
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
public class EducationServiceImpl extends AbstractBaseService<Education, EducationDto, Integer> implements EducationService {
    @Autowired
    EducationRepository educationRepository;

    @Autowired
    EducationMapper educationMapper;

    @Autowired
    UsersService usersService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CvService cvService;

    @Autowired
    CvRepository cvRepository;

    public EducationServiceImpl(EducationRepository educationRepository, EducationMapper educationMapper) {
        super(educationRepository, educationMapper, educationRepository::findById);
        this.educationRepository = educationRepository;
        this.educationMapper = educationMapper;
    }

    @Override
    public EducationDto createEducation(Integer id, EducationDto dto) {
        Education education = educationMapper.mapDtoToEntity(dto);
        Cv entity = cvRepository.getById(id);
        education.setCv(entity);
        education.setStatus(BasicStatus.ACTIVE);
        Education saved = educationRepository.save(education);
        return educationMapper.mapEntityToDto(saved);
    }

    @Override
    public boolean updateEducation(int cvId, int educationId, EducationDto dto) {
        Optional<Education> existingEducationOptional = educationRepository.findById(educationId);
        if (existingEducationOptional.isPresent()) {
            Education existingEducation = existingEducationOptional.get();
            if (existingEducation.getCv().getId() != cvId) {
                throw new IllegalArgumentException("Education does not belong to Cv with id " + cvId);
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
            if (dto.getGpa() > 1 && existingEducation.getGpa() != dto.getGpa()) {
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
    public List<EducationDto> getAllEducation(int cvId) {
        List<Education> educations = educationRepository.findEducationsByStatus(cvId, BasicStatus.ACTIVE);
        return educations.stream()
                .filter(education -> education.getStatus() == BasicStatus.ACTIVE)
                .map(education -> {
                    EducationDto educationDto = new EducationDto();
                    educationDto.setId(education.getId());
                    educationDto.setDegree(education.getDegree());
                    educationDto.setCollegeName(education.getCollegeName());
                    educationDto.setLocation(education.getLocation());
                    educationDto.setEndYear(education.getEndYear());
                    educationDto.setMinor(education.getMinor());
                    educationDto.setGpa(education.getGpa());
                    educationDto.setDescription(education.getDescription());
                    return educationDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteEducationById(Integer UsersId, Integer educationId) {
        boolean isEducationBelongsToCv = educationRepository.existsByIdAndCv_Id(educationId, UsersId);

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

    @Override
    public EducationDto getAndIsDisplay(int cvId, int id) throws JsonProcessingException {
        Education education = educationRepository.getById(id);
        if (Objects.nonNull(education)) {
            Cv cv = cvService.getCvById(cvId);
            CvBodyDto cvBodyDto = cv.deserialize();
            Optional<EducationDto> dto = cvBodyDto.getEducations().stream().filter(x -> x.getId() == id).findFirst();
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
    public EducationDto getByIdInCvBody(int cvId, int id) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        Optional<EducationDto> dto = cvBodyDto.getEducations().stream().filter(x -> x.getId() == id).findFirst();
        if (dto.isPresent()) {
            return dto.get();
        } else {
            throw new ResourceNotFoundException("Not found that id in cvBody");
        }
    }

    @Override
    public List<EducationDto> getAllARelationInCvBody(int cvId) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        List<EducationDto> set = new ArrayList<>();
        cvBodyDto.getEducations().stream().forEach(
                e -> {
                    try {
                        set.add(getAndIsDisplay(cvId, e.getId()));
                    } catch (JsonProcessingException ex) {
                        throw new RuntimeException(ex);
                    }
                }
        );
        return set;
    }


    @Override
    public boolean updateInCvBody(int cvId, int id, EducationDto dto) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        Optional<EducationDto> relationDto = cvBodyDto.getEducations().stream().filter(x -> x.getId() == id).findFirst();
        if (relationDto.isPresent()) {
            Education education = educationRepository.getById(id);
            modelMapper.map(dto, education);
            educationRepository.save(education);
            EducationDto educationDto = relationDto.get();
            modelMapper.map(dto, educationDto);
            cvService.updateCvBody(cvId, cvBodyDto);
            return true;
        } else {
            throw new IllegalArgumentException("education ID not found in cvBody");
        }
    }


    @Override
    public EducationDto createOfUserInCvBody(int cvId, EducationDto dto) throws JsonProcessingException {
        Education education = educationMapper.mapDtoToEntity(dto);
        Cv cv = cvRepository.findCvById(cvId, BasicStatus.ACTIVE);
        education.setCv(cv);
        education.setStatus(BasicStatus.ACTIVE);
        Education saved = educationRepository.save(education);
        EducationDto educationDto = new EducationDto();
        educationDto.setId(saved.getId());

        List<Cv> list = cvRepository.findAllByUsersIdAndStatus(cv.getUser().getId(), BasicStatus.ACTIVE);
        list.stream().forEach(x -> {
            if (x.getId().equals(cvId)) {
                educationDto.setIsDisplay(true);
            } else {
                educationDto.setIsDisplay(false);
            }
            try {
                CvBodyDto cvBodyDto = x.deserialize();
                educationDto.setTheOrder(cvBodyDto.getEducations().size() + 1);
                cvBodyDto.getEducations().add(educationDto);
                cvService.updateCvBody(x.getId(), cvBodyDto);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        return educationMapper.mapEntityToDto(saved);
    }

    @Override
    public void deleteInCvBody(Integer cvId, Integer educationId) throws JsonProcessingException {
        Optional<Education> Optional = educationRepository.findById(educationId);
        if (Optional.isPresent()) {
            Education education = Optional.get();
            education.setStatus(BasicStatus.DELETED);
            educationRepository.delete(education);
            try {
                CvBodyDto cvBodyDto = cvService.getCvBody(cvId);
                EducationDto dto = cvBodyDto.getEducations().stream().filter(e-> e.getId().equals(educationId)).findFirst().get();
                cvBodyDto.getEducations().forEach(c -> {
                    if (Objects.nonNull(c.getTheOrder()) && c.getTheOrder() > dto.getTheOrder()){
                        c.setTheOrder(c.getTheOrder() - 1);
                    }
                });
                cvBodyDto.getEducations().removeIf(e -> e.getId() == educationId);
                cvService.updateCvBody(cvId, cvBodyDto);

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

