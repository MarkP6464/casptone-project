package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.exception.ResourceNotFoundException;
import com.example.capstoneproject.mapper.ExperienceMapper;
import com.example.capstoneproject.repository.ExperienceRepository;
import com.example.capstoneproject.service.CvService;
import com.example.capstoneproject.service.UsersService;
import com.example.capstoneproject.service.ExperienceService;
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
public class ExperienceServiceImpl extends AbstractBaseService<Experience, ExperienceDto, Integer> implements ExperienceService {
    @Autowired
    ExperienceRepository experienceRepository;

    @Autowired
    CvService cvService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ExperienceMapper experienceMapper;

    @Autowired
    UsersService usersService;

    public ExperienceServiceImpl(ExperienceRepository experienceRepository, ExperienceMapper experienceMapper) {
        super(experienceRepository, experienceMapper, experienceRepository::findById);
        this.experienceRepository = experienceRepository;
        this.experienceMapper = experienceMapper;
    }

    @Override
    public ExperienceDto createExperience(Integer id, ExperienceDto dto) {
        Experience experience = experienceMapper.mapDtoToEntity(dto);
        Users Users = usersService.getUsersById(id);
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
    public List<ExperienceDto> getAllExperience(int UsersId) {
        List<Experience> experiences = experienceRepository.findExperiencesByStatus(UsersId, BasicStatus.ACTIVE);
        return experiences.stream()
                .filter(experience -> experience.getStatus() == BasicStatus.ACTIVE)
                .map(experience -> {
                    ExperienceDto ExperienceDto = new ExperienceDto();
                    ExperienceDto.setId(experience.getId());
                    ExperienceDto.setRole(experience.getRole());
                    ExperienceDto.setCompanyName(experience.getCompanyName());
                    ExperienceDto.setStartDate(experience.getStartDate());
                    ExperienceDto.setEndDate(experience.getEndDate());
                    ExperienceDto.setLocation(experience.getLocation());
                    ExperienceDto.setDescription(experience.getDescription());
                    return ExperienceDto;
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

    @Override
    public ExperienceDto getAndIsDisplay(int cvId, int id) throws JsonProcessingException {
        Experience education = experienceRepository.getById(id);
        if (Objects.nonNull(education)){
            Cv cv = cvService.getCvById(cvId);
            CvBodyDto cvBodyDto = cv.deserialize();
            Optional<ExperienceDto> dto = cvBodyDto.getExperiences().stream().filter(x -> x.getId()==id).findFirst();
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
    public ExperienceDto getByIdInCvBody(int cvId, int id) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        Optional<ExperienceDto> dto = cvBodyDto.getExperiences().stream().filter(x -> x.getId()==id).findFirst();
        if (dto.isPresent()){
            return  dto.get();
        }else{
            throw new ResourceNotFoundException("Not found that id in cvBody");
        }
    }

    @Override
    public Set<ExperienceDto> getAllARelationInCvBody(int cvId) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        return cvBodyDto.getExperiences();
    }

    @Override
    public boolean updateInCvBody(int cvId, int id, ExperienceDto dto) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        Optional<ExperienceDto> relationDto = cvBodyDto.getExperiences().stream().filter(x -> x.getId()==id).findFirst();
        if (relationDto.isPresent()) {
            Experience education = experienceRepository.getById(id);
            modelMapper.map(dto, education);
            experienceRepository.save(education);
            ExperienceDto educationDto = relationDto.get();
            educationDto.setIsDisplay(dto.getIsDisplay());
            cvService.updateCvBody(0, cvId, cvBodyDto);
            return true;
        } else {
            throw new IllegalArgumentException("education ID not found in cvBody");
        }
    }


    @Override
    public ExperienceDto createOfUserInCvBody(int cvId, ExperienceDto dto) throws JsonProcessingException {
        Experience education = experienceMapper.mapDtoToEntity(dto);
        Users Users = usersService.getUsersById(cvId);
        education.setUser(Users);
        education.setStatus(BasicStatus.ACTIVE);
        Experience saved = experienceRepository.save(education);
        ExperienceDto educationViewDto = new ExperienceDto();
        educationViewDto.setId(saved.getId());
        CvBodyDto cvBodyDto = cvService.getCvBody(cvId);
        cvBodyDto.getExperiences().add(educationViewDto);
        cvService.updateCvBody(0, cvId, cvBodyDto);
        return educationViewDto;
    }

    @Override
    public void deleteInCvBody(Integer cvId, Integer educationId) throws JsonProcessingException {

        Optional<Experience> Optional = experienceRepository.findById(educationId);
        if (Optional.isPresent()) {
            Experience education = Optional.get();
            education.setStatus(BasicStatus.DELETED);
            experienceRepository.save(education);
            CvBodyDto cvBodyDto = cvService.getCvBody(cvId);
            cvBodyDto.getEducations().removeIf(x -> x.getId() == educationId);
            cvService.updateCvBody(0, cvId, cvBodyDto);
        }
    }

}
