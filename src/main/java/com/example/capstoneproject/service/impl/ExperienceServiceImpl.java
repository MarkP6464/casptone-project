package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.ExperienceViewDto;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.Evaluate;
import com.example.capstoneproject.entity.Experience;
import com.example.capstoneproject.entity.Section;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.SectionEvaluate;
import com.example.capstoneproject.exception.BadRequestException;
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
    ScoreRepository scoreRepository;

    @Autowired
    ScoreLogRepository scoreLogRepository;

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
        return null;
    }

    @Override
    public boolean updateExperience(Integer UsersId, Integer experienceId, ExperienceDto dto) {
        Optional<Experience> existingExperienceOptional = experienceRepository.findById(experienceId);
        if (existingExperienceOptional.isPresent()) {
            Experience existingExperience = existingExperienceOptional.get();
            if (existingExperience.getCv().getId() != UsersId) {
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
    public List<ExperienceDto> getAllExperience(Integer cvId) {
        List<Experience> experiences = experienceRepository.findExperiencesByStatus(cvId, BasicStatus.ACTIVE);
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
        boolean isExperienceBelongsToCv = experienceRepository.existsByIdAndCv_User_Id(experienceId, UsersId);

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
                experienceViewDto.setId(experienceDto.getId());
                experienceViewDto.setIsDisplay(experienceDto.getIsDisplay());
                experienceViewDto.setRole(experienceDto.getRole());
                experienceViewDto.setTheOrder(experienceDto.getTheOrder());
                experienceViewDto.setCompanyName(experienceDto.getCompanyName());
                experienceViewDto.setDuration(experienceDto.getDuration());
                experienceViewDto.setLocation(experienceDto.getLocation());
                experienceViewDto.setDescription(experienceDto.getDescription());
                experienceViewDto.setBulletPointDtos(bulletPointDtos);
                experienceViewDto.setStatus(experienceDto.getStatus());
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
        List<ExperienceViewDto> list = set.stream().filter(x -> x.getStatus().equals(BasicStatus.ACTIVE)).collect(Collectors.toList());
        return list;
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
            modelMapper.map(dto, experienceDto);
            experienceDto.setTheOrder(dto.getTheOrder());
            cvService.updateCvBody(cvId, cvBodyDto);


            //Delete section_log in db
            Section section = sectionRepository.findByTypeNameAndTypeId(SectionEvaluate.experience, experience.getId());

//            Optional<Score> scoreOptional = scoreRepository.findByCv_Id(cvId);
//            if(scoreOptional.isPresent()){
//                Score score = scoreOptional.get();
//                //Delete score in db
//                scoreLogRepository.deleteAllByScore_Id(score.getId());
//
//                //Delete score in db
//                scoreRepository.deleteScoreById(score.getId());
//            }

            if (section != null) {
                sectionLogRepository.deleteBySection_Id(section.getId());
                cv.setOverview(null);
                cvRepository.save(cv);
            }
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
                sectionLogDto1.setSection(section);
                sectionLogDto1.setEvaluate(evaluate);
                sectionLogDto1.setBullet(bulletPointDto.getResult());
                sectionLogDto1.setCount(bulletPointDto.getCount());
                sectionLogDto1.setStatus(bulletPointDto.getStatus());
                sectionLogService.create(sectionLogDto1);
                evaluateId++;
                if (evaluateId == 9) {
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
            Experience experience = Optional.get();
            experience.setStatus(BasicStatus.DELETED);
            experienceRepository.save(experience);
            try {
                CvBodyDto cvBodyDto = cvService.getCvBody(cvId);
                ExperienceDto dto = cvBodyDto.getExperiences()
                        .stream().filter(e -> e.getId().equals(educationId)).findFirst().get();
                cvBodyDto.getExperiences().forEach(c -> {
                    if (Objects.nonNull(c.getTheOrder()) && c.getTheOrder() > dto.getTheOrder()) {
                        c.setTheOrder(c.getTheOrder() - 1);
                    }
                });
                dto.setIsDisplay(false);
                dto.setTheOrder(null);
                dto.setStatus(BasicStatus.DELETED);
                cvService.updateCvBody(cvId, cvBodyDto);

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public ExperienceViewDto createOfUserInCvBody(int cvId, ExperienceDto dto) throws JsonProcessingException {
        Experience experience = experienceMapper.mapDtoToEntity(dto);
        Cv cv = cvService.getCvById(cvId);
        if (cv != null) {
            experience.setCv(cv);
        } else {
            throw new BadRequestException("Cv id not found.");
        }
        experience.setStatus(BasicStatus.ACTIVE);
        Experience saved = experienceRepository.save(experience);
        dto.setId(saved.getId());
        dto.setStatus(BasicStatus.ACTIVE);
        CvBodyDto cvBodyDto = cv.deserialize();
        Integer activeEdus = cvBodyDto.getExperiences().stream()
                .filter(x -> Objects.nonNull(x.getIsDisplay()) && x.getIsDisplay().equals(true)).collect(Collectors.toList()).size();
        dto.setTheOrder(activeEdus + 1);
        cvBodyDto.getExperiences().add(dto);
        cvService.updateCvBody(cv.getId(), cvBodyDto);
        //Save evaluate db
        SectionDto sectionDto = new SectionDto();
        List<Experience> experiences = experienceRepository.findExperiencesByStatusOrderedByStartDateDesc(cv.getId(), BasicStatus.ACTIVE);
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
            sectionLogDto1.setCount(bulletPointDto.getCount());
            sectionLogDto1.setStatus(bulletPointDto.getStatus());
            sectionLogService.create(sectionLogDto1);
            evaluateId++;
            if (evaluateId == 9) {
                break;
            }
        }
        return experienceViewDto;
    }

}
