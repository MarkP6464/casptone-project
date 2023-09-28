package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.mapper.CvMapper;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
@Service
public class CvServiceImpl extends AbstractBaseService<Cv, CvDto, Integer> implements CvService {
    @Autowired
    CvRepository cvRepository;

    @Autowired
    @Lazy
    EducationService educationService;

    @Autowired
    @Lazy
    SkillService skillService;
    @Autowired
    @Lazy
    SourceWorkService sourceWorkService;
    @Autowired
    @Lazy
    ExperienceService experienceService;
    @Autowired
    @Lazy
    InvolvementService involvementService;
    @Autowired
    @Lazy
    ProjectService projectService;

    @Autowired
    EducationRepository educationRepository;
    @Autowired
    SkillRepository skillRepository;
    @Autowired
    ExperienceRepository experienceRepository;
    @Autowired
    CertificationRepository certificationRepository;
    @Autowired
    InvolvementRepository involvementRepository;
    @Autowired
    SourceWorkRepository sourceWorkRepository;
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    @Lazy
    CertificationService certificationService;

    @Autowired
    CvMapper cvMapper;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UsersRepository UsersRepository;

    @Autowired
    TemplateRepository templateRepository;


    public CvServiceImpl(CvRepository cvRepository, CvMapper cvMapper) {
        super(cvRepository, cvMapper, cvRepository::findById);
        this.cvRepository = cvRepository;
        this.cvMapper = cvMapper;
    }


    @Override
    public List<CvDto> GetCvsById(int UsersId) {
        return cvRepository.findAllByUsersIdAndStatus(UsersId, BasicStatus.ACTIVE)
                .stream()
                .map(cv -> {
                    CvDto cvDto = new CvDto();
                    cvDto.setId(cv.getId());
                    cvDto.setContent(cv.getContent());
                    cvDto.setSummary(cv.getSummary());
                    Users Users = cv.getUser();
                    if (Users != null) {
                        UsersViewDto UsersViewDto = new UsersViewDto();
                        UsersViewDto.setId(Users.getId());
                        UsersViewDto.setName(Users.getName());
                        cvDto.setUsers(UsersViewDto);
                    }
                    Template template = cv.getTemplate();
                    if (template != null) {
                        TemplateViewDto templateViewDto = new TemplateViewDto();
                        templateViewDto.setId(template.getId());
                        templateViewDto.setName(template.getName());
                        templateViewDto.setContent(template.getContent());
                        templateViewDto.setAmountView(template.getAmountView());
                        cvDto.setTemplate(templateViewDto);
                    }
//                    cvDto.setCertifications(
//                            cv.getCertifications().stream()
//                                    .filter(certification -> certification.getStatus() == CvStatus.ACTIVE)
//                                    .map(certification -> {
//                                        CertificationViewDto certificationViewDto = new CertificationViewDto();
//                                        certificationViewDto.setId(certification.getId());
//                                        certificationViewDto.setName(certification.getName());
//                                        certificationViewDto.setCertificateSource(certification.getCertificateSource());
//                                        certificationViewDto.setEndYear(certification.getEndYear());
//                                        certificationViewDto.setCertificateRelevance(certification.getCertificateRelevance());
//                                        return certificationViewDto;
//                                    })
//                                    .collect(Collectors.toList())
//                    );
                    return cvDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CvDto GetCvsByCvId(int UsersId, int cvId) {
        Cv cv = cvRepository.findCvByIdAndStatus(UsersId, cvId, BasicStatus.ACTIVE);
        if (cv != null) {
            CvDto cvDto = new CvDto();
            cvDto.setId(cv.getId());
            cvDto.setContent(cv.getContent());
            cvDto.setSummary(cv.getSummary());
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                cvDto.setCvBody(cv.deserialize());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            Users Users = cv.getUser();
            if (Users != null) {
                UsersViewDto UsersViewDto = new UsersViewDto();
                UsersViewDto.setId(Users.getId());
                UsersViewDto.setName(Users.getName());
                cvDto.setUsers(UsersViewDto);
            }
            Template template = cv.getTemplate();
            if (template != null) {
                TemplateViewDto templateViewDto = new TemplateViewDto();
                templateViewDto.setId(template.getId());
                templateViewDto.setName(template.getName());
                templateViewDto.setContent(template.getContent());
                templateViewDto.setAmountView(template.getAmountView());
                cvDto.setTemplate(templateViewDto);
            }

            return cvDto;
        } else {
            throw new IllegalArgumentException("CV not found with cvId: " + cvId);
        }
    }

    @Override
    public void deleteCvById(Integer UsersId, Integer id) {
        Optional<Users> UsersOptional = UsersRepository.findById(UsersId);

        if (UsersOptional.isPresent()) {
            Optional<Cv> cvOptional = cvRepository.findByIdAndUserId(id, UsersId);

            if (cvOptional.isPresent()) {
                Cv cv = cvOptional.get();
                cv.setStatus(BasicStatus.DELETED);
                cvRepository.save(cv);
            } else {
                throw new IllegalArgumentException("CV not found with id: " + id);
            }
        } else {
            throw new IllegalArgumentException("Users not found with id: " + UsersId);
        }
    }


    @Override
    public CvAddNewDto createCv(Integer UsersId, CvAddNewDto dto) {
        Optional<Users> UsersOptional = UsersRepository.findById(UsersId);

        if (UsersOptional.isPresent()) {
            Cv cv = new Cv();
            cv.setContent(dto.getContent());
            cv.setStatus(BasicStatus.ACTIVE);
            Users Users = UsersOptional.get();
            cv.setUser(Users);
            Cv savedCv = cvRepository.save(cv);
            CvAddNewDto createdDto = new CvAddNewDto();
            createdDto.setContent(savedCv.getContent());

            return createdDto;
        } else {
            throw new IllegalArgumentException("Not found user with ID: " + UsersId);
        }
    }


    @Override
    public Cv getCvById(int cvId) {
        Optional<Cv> cvOptional = cvRepository.findById(cvId);
        if (cvOptional.isPresent()) {
            return cvOptional.get();
        } else {
            throw new IllegalArgumentException("CV not found with ID: " + cvId);
        }
    }

    @Override
    public boolean updateCvSummary(int UsersId, int cvId, CvUpdateSumDto dto) {
        Optional<Users> UsersOptional = UsersRepository.findById(UsersId);

        if (UsersOptional.isPresent()) {
            Optional<Cv> cvOptional = cvRepository.findById(cvId);

            if (cvOptional.isPresent()) {
                Cv cv = cvOptional.get();
                cv.setSummary(dto.getSummary());

                cvRepository.save(cv);

                return true;
            } else {
                throw new IllegalArgumentException("CvId not found: " + cvId);
            }
        } else {
            throw new IllegalArgumentException("UsersId not found: " + UsersId);
        }
    }

    @Override
    public boolean updateCvBody(int cvId, CvBodyDto dto) throws JsonProcessingException {
            Optional<Cv> cvOptional = cvRepository.findById(cvId);
            if (cvOptional.isPresent()) {
                Cv cv = cvOptional.get();
                cv.toCvBody(dto);
                cvRepository.save(cv);
                return true;
            } else {
                throw new IllegalArgumentException("CvId not found: " + cvId);
            }
    }

    @Override
    public boolean updateCvContent(int UsersId, int cvId, CvAddNewDto dto) {
        Optional<Users> UsersOptional = UsersRepository.findById(UsersId);

        if (UsersOptional.isPresent()) {
            Optional<Cv> cvOptional = cvRepository.findById(cvId);

            if (cvOptional.isPresent()) {
                Cv cv = cvOptional.get();
                cv.setContent(dto.getContent());

                cvRepository.save(cv);

                return true;
            } else {
                throw new IllegalArgumentException("CvId not found: " + cvId);
            }
        } else {
            throw new IllegalArgumentException("UsersId not found: " + UsersId);
        }
    }

    @Override
    public boolean updateCvContact(int UsersId, int cvId, int contactId) {
        return false;
    }

    @Override
    public boolean updateCvTemplate(int UsersId, int cvId, int templateId) {
        Optional<Users> UsersOptional = UsersRepository.findById(UsersId);

        if (UsersOptional.isPresent()) {
            Optional<Cv> cvOptional = cvRepository.findById(cvId);

            if (cvOptional.isPresent()) {
                Cv cv = cvOptional.get();
                Optional<Template> templateOptional = templateRepository.findById(templateId);

                if (templateOptional.isPresent()) {
                    Template template = templateOptional.get();
                    cv.setTemplate(template);

                    cvRepository.save(cv);

                    return true;
                } else {
                    throw new IllegalArgumentException("TemplateId not found: " + templateId);
                }
            } else {
                throw new IllegalArgumentException("CvId not found: " + cvId);
            }
        } else {
            throw new IllegalArgumentException("UsersId not found: " + UsersId);
        }
    }

    @Override
    public CvBodyDto getCvBody(int cvId) throws JsonProcessingException {
        return getCvById(cvId).deserialize();
    }

    @Override
    public CvDto synchUp(int cvId) throws JsonProcessingException {
        Cv cv = cvRepository.getById(cvId);
        if (Objects.nonNull(cv)){
            CvBodyDto cvBodyDto = cv.deserialize();
            cvBodyDto.getEducations().forEach(x -> {
                Education e = educationRepository.findById(x.getId().intValue()).get();
                modelMapper.map(e, x);
            });
            cvBodyDto.getSkills().forEach(x -> {
                Skill e = skillRepository.findById(x.getId().intValue()).get();
                modelMapper.map(e, x);
            });
            cvBodyDto.getExperiences().forEach(x -> {
                Experience e = experienceRepository.findById(x.getId().intValue()).get();
                modelMapper.map(e, x);
            });
            cvBodyDto.getInvolvements().forEach(x -> {
                Involvement e = involvementRepository.findById(x.getId().intValue()).get();
                modelMapper.map(e, x);
            });
            cvBodyDto.getCertifications().forEach(x -> {
                Certification e = certificationRepository.findById(x.getId().intValue()).get();
                modelMapper.map(e, x);
            });
            cvBodyDto.getProjects().forEach(x -> {
                Project e = projectRepository.findById(x.getId().intValue()).get();
                modelMapper.map(e, x);
            });
            cvBodyDto.getSourceWorks().forEach(x -> {
                SourceWork e = sourceWorkRepository.findById(x.getId().intValue()).get();
                modelMapper.map(e, x);
            });
            updateCvBody(cvId, cvBodyDto);
        }
        return cvMapper.mapEntityToDto(cv);
    }

}
