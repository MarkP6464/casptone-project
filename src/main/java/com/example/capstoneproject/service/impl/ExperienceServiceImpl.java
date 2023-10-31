package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.ExperienceViewDto;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.SectionEvaluate;
import com.example.capstoneproject.exception.ResourceNotFoundException;
import com.example.capstoneproject.mapper.ExperienceMapper;
import com.example.capstoneproject.mapper.SectionMapper;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.*;
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
public class ExperienceServiceImpl extends AbstractBaseService<Experience, ExperienceDto, Integer> implements ExperienceService {
    @Autowired
    ExperienceRepository experienceRepository;

    @Autowired
    CvService cvService;

    @Autowired
    EvaluateRepository evaluateRepository;

    @Autowired
    SectionService sectionService;

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    SectionMapper sectionMapper;

    @Autowired
    SectionLogRepository sectionLogRepository;

    @Autowired
    SectionLogService sectionLogService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ExperienceMapper experienceMapper;

    @Autowired
    UsersService usersService;

    @Autowired
    EvaluateService evaluateService;

    @Autowired
    CvRepository cvRepository;

    public ExperienceServiceImpl(ExperienceRepository experienceRepository, ExperienceMapper experienceMapper) {
        super(experienceRepository, experienceMapper, experienceRepository::findById);
        this.experienceRepository = experienceRepository;
        this.experienceMapper = experienceMapper;
    }

    @Override
    public ExperienceViewDto createExperience(Integer id, ExperienceDto dto) {
//        Experience experience = experienceMapper.mapDtoToEntity(dto);
//        Users Users = usersService.getUsersById(id);
//        experience.setUser(Users);
//        experience.setStatus(BasicStatus.ACTIVE);
//        Experience saved = experienceRepository.save(experience);
//
//        //Save evaluate db
//        SectionDto sectionDto = new SectionDto();
//        List<Experience> experiences = experienceRepository.findExperiencesByStatusOrderedByStartDateDesc(id, BasicStatus.ACTIVE);
//        if (!experiences.isEmpty()) {
//            sectionDto.setTypeId(experiences.get(0).getId());
//        }
//        sectionDto.setTitle(saved.getRole());
//        sectionDto.setTypeName(SectionEvaluate.experience);
//        SectionDto section = sectionService.create(sectionDto);
//
//        //Get process evaluate
//        List<BulletPointDto> evaluateResult = evaluateService.checkSentences(dto.getDescription());
//        ExperienceViewDto experienceViewDto = new ExperienceViewDto();
//        experienceViewDto.setId(saved.getId());
//        experienceViewDto.setRole(saved.getRole());
//        experienceViewDto.setCompanyName(saved.getCompanyName());
//        experienceViewDto.setStartDate(saved.getStartDate());
//        experienceViewDto.setEndDate(saved.getEndDate());
//        experienceViewDto.setLocation(saved.getLocation());
//        experienceViewDto.setDescription(saved.getDescription());
//        experienceViewDto.setBulletPointDtos(evaluateResult);
//
//        //Save evaluateLog into db
//        List<Evaluate> evaluates = evaluateRepository.findAll();
//
//        for (int i = 0; i < evaluates.size(); i++) {
//            Evaluate evaluate = evaluates.get(i);
//            BulletPointDto bulletPointDto = evaluateResult.get(i);
//            SectionLogDto sectionLogDto1 = new SectionLogDto();
//            sectionLogDto1.setSection(sectionMapper.mapDtoToEntity(section));
//            sectionLogDto1.setEvaluate(evaluate);
//            sectionLogDto1.setBullet(bulletPointDto.getResult());
//            sectionLogDto1.setStatus(bulletPointDto.getStatus());
//            sectionLogService.create(sectionLogDto1);
//        }
        return null;
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
            if (dto.getDuration() != null && !existingExperience.getDuration().equals(dto.getDuration())) {
                existingExperience.setDuration(dto.getDuration());
            } else {
                existingExperience.setDuration(existingExperience.getDuration());
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
                    ExperienceDto.setDuration(experience.getDuration());
                    ExperienceDto.setLocation(experience.getLocation());
                    ExperienceDto.setDescription(experience.getDescription());
                    return ExperienceDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteExperienceById(Integer UsersId, Integer experienceId) {
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
    public ExperienceViewDto getAndIsDisplay(int cvId, int id) throws JsonProcessingException {
        Experience experience = experienceRepository.getById(id);
        if (Objects.nonNull(experience)) {
            Cv cv = cvService.getCvById(cvId);
            CvBodyDto cvBodyDto = cv.deserialize();
            Optional<ExperienceDto> dto = cvBodyDto.getExperiences().stream().filter(x -> x.getId() == id).findFirst();
            List<BulletPointDto> bulletPointDtos = sectionRepository.findBulletPointDtoByTypeIdAndTypeName(id, SectionEvaluate.experience);
            if (dto.isPresent()) {
                ExperienceDto experienceDto = dto.get();
                ExperienceViewDto experienceViewDto = new ExperienceViewDto();
                experienceViewDto.setId(experience.getId());
                experienceViewDto.setIsDisplay(experienceDto.getIsDisplay());
                experienceViewDto.setRole(experience.getRole());
                experienceViewDto.setCompanyName(experience.getCompanyName());
                experienceViewDto.setDuration(experience.getDuration());
                experienceViewDto.setLocation(experience.getLocation());
                experienceViewDto.setDescription(experience.getDescription());
                experienceViewDto.setBulletPointDtos(bulletPointDtos);
                return experienceViewDto;
            } else {
                throw new ResourceNotFoundException("Not found that id in cvBody");
            }
        } else {
            throw new ResourceNotFoundException("Not found that id in cvBody");
        }
    }

    @Override
    public ExperienceDto getByIdInCvBody(int cvId, int id) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        Optional<ExperienceDto> dto = cvBodyDto.getExperiences().stream().filter(x -> x.getId() == id).findFirst();
        if (dto.isPresent()) {
            return dto.get();
        } else {
            throw new ResourceNotFoundException("Not found that id in cvBody");
        }
    }

    @Override
    public List<ExperienceViewDto> getAllARelationInCvBody(int cvId) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        List<ExperienceViewDto> set = new ArrayList<>();
        cvBodyDto.getExperiences().stream().forEach(
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
    public ExperienceViewDto updateInCvBody(int cvId, int id, ExperienceDto dto) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        Optional<ExperienceDto> relationDto = cvBodyDto.getExperiences().stream().filter(x -> x.getId() == id).findFirst();
        if (relationDto.isPresent()) {
            Experience experience = experienceRepository.getById(id);
            modelMapper.map(dto, experience);
            Experience saved = experienceRepository.save(experience);
            ExperienceDto experienceDto = relationDto.get();
            experienceDto.setIsDisplay(dto.getIsDisplay());
            cvService.updateCvBody(cvId, cvBodyDto);

            //Delete section_log in db
            Section section = sectionRepository.findByTypeNameAndTypeId(SectionEvaluate.experience, experience.getId());
            sectionLogRepository.deleteBySection_Id(section.getId());
            //Get process evaluate
            List<BulletPointDto> evaluateResult = evaluateService.checkSentences(dto.getDescription());
            ExperienceViewDto experienceViewDto = new ExperienceViewDto();
            experienceViewDto.setId(saved.getId());
            experienceViewDto.setIsDisplay(dto.getIsDisplay());
            experienceViewDto.setRole(saved.getRole());
            experienceViewDto.setCompanyName(saved.getCompanyName());
            experienceViewDto.setDuration(saved.getDuration());
            experienceViewDto.setLocation(saved.getLocation());
            experienceViewDto.setDescription(saved.getDescription());
            experienceViewDto.setBulletPointDtos(evaluateResult);

            //Save evaluateLog into db
            List<Evaluate> evaluates = evaluateRepository.findAll();

            int evaluateId = 1;
            for (int i = 0; i < evaluates.size(); i++) {
                Evaluate evaluate = evaluates.get(i);
                BulletPointDto bulletPointDto = evaluateResult.get(i);
                SectionLogDto sectionLogDto1 = new SectionLogDto();
                sectionLogDto1.setSection(sectionMapper.mapDtoToEntity(sectionMapper.mapEntityToDto(section)));
                sectionLogDto1.setEvaluate(evaluate);
                sectionLogDto1.setBullet(bulletPointDto.getResult());
                sectionLogDto1.setStatus(bulletPointDto.getStatus());
                sectionLogService.create(sectionLogDto1);
                evaluateId++;
                if (evaluateId == 7) {
                    break;
                }
            }
            return experienceViewDto;

        } else {
            throw new IllegalArgumentException("education ID not found in cvBody");
        }
    }

    @Override
    public void deleteInCvBody(Integer cvId, Integer educationId) throws JsonProcessingException {

        Optional<Experience> Optional = experienceRepository.findById(educationId);
        if (Optional.isPresent()) {
            Experience education = Optional.get();
            education.setStatus(BasicStatus.DELETED);
            experienceRepository.delete(education);
            List<Cv> list = cvRepository.findAllByUsersIdAndStatus(education.getUser().getId(), BasicStatus.ACTIVE);
            list.stream().forEach(x -> {
                try {
                    CvBodyDto cvBodyDto = x.deserialize();
                    cvBodyDto.getExperiences().removeIf(e -> e.getId() == educationId);
                    cvService.updateCvBody(x.getId(), cvBodyDto);

                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Override
    public ExperienceViewDto createOfUserInCvBody(int cvId, ExperienceDto dto) throws JsonProcessingException {
        Experience education = experienceMapper.mapDtoToEntity(dto);
        Users user = usersService.getUsersById(cvService.getCvById(cvId).getUser().getId());
        education.setUser(user);
        education.setStatus(BasicStatus.ACTIVE);
        Experience saved = experienceRepository.save(education);
        ExperienceDto educationViewDto = new ExperienceDto();
        educationViewDto.setId(saved.getId());

        List<Cv> list = cvRepository.findAllByUsersIdAndStatus(user.getId(), BasicStatus.ACTIVE);
        list.stream().forEach(x -> {
            if (x.getId().equals(cvId)) {
                educationViewDto.setIsDisplay(true);
            } else {
                educationViewDto.setIsDisplay(false);
            }
            educationViewDto.setTheOrder(list.size() + 1);
            try {
                CvBodyDto cvBodyDto = x.deserialize();
                cvBodyDto.getExperiences().add(educationViewDto);
                cvService.updateCvBody(x.getId(), cvBodyDto);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        //Save evaluate db
        SectionDto sectionDto = new SectionDto();
        List<Experience> experiences = experienceRepository.findExperiencesByStatusOrderedByStartDateDesc(user.getId(), BasicStatus.ACTIVE);
        if (!experiences.isEmpty()) {
            sectionDto.setTypeId(experiences.get(0).getId());
        }
        sectionDto.setTitle(saved.getRole());
        sectionDto.setTypeName(SectionEvaluate.experience);
        SectionDto section = sectionService.create(sectionDto);

        //Get process evaluate
        List<BulletPointDto> evaluateResult = evaluateService.checkSentences(dto.getDescription());
        ExperienceViewDto experienceViewDto = new ExperienceViewDto();
        experienceViewDto.setId(saved.getId());
        experienceViewDto.setIsDisplay(true);
        experienceViewDto.setRole(saved.getRole());
        experienceViewDto.setCompanyName(saved.getCompanyName());
        experienceViewDto.setDuration(saved.getDuration());
        experienceViewDto.setLocation(saved.getLocation());
        experienceViewDto.setDescription(saved.getDescription());
        experienceViewDto.setBulletPointDtos(evaluateResult);

        //Save evaluateLog into db
        List<Evaluate> evaluates = evaluateRepository.findAll();

        int evaluateId = 1;
        for (int i = 0; i < evaluates.size(); i++) {
            Evaluate evaluate = evaluates.get(i);
            BulletPointDto bulletPointDto = evaluateResult.get(i);
            SectionLogDto sectionLogDto1 = new SectionLogDto();
            sectionLogDto1.setSection(sectionMapper.mapDtoToEntity(section));
            sectionLogDto1.setEvaluate(evaluate);
            sectionLogDto1.setBullet(bulletPointDto.getResult());
            sectionLogDto1.setStatus(bulletPointDto.getStatus());
            sectionLogService.create(sectionLogDto1);
            evaluateId++;
            if (evaluateId == 7) {
                break;
            }
        }
        return experienceViewDto;
    }

}
