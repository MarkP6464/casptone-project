package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.mapper.CvMapper;
import com.example.capstoneproject.mapper.UsersMapper;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CvServiceImpl extends AbstractBaseService<Cv, CvDto, Integer> implements CvService {
    @Autowired
    CvRepository cvRepository;

    @Autowired
    UsersMapper usersMapper;
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
    UsersRepository usersRepository;

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
    public CvAddNewDto GetCvByCvId(int UsersId, int cvId) throws JsonProcessingException {
        Cv cv = cvRepository.findCvByIdAndStatus(UsersId, cvId, BasicStatus.ACTIVE);
        if (cv != null) {
            CvAddNewDto cvDto = cvMapper.cvAddNewDto(cv);
            UsersViewDto usersViewDto = usersMapper.toView(cv.getUser());
            CvBodyDto cvBodyDto = cv.deserialize();
            cvDto.setCvStyle(cvBodyDto.getCvStyle());
            cvDto.setTemplateType(cvBodyDto.getTemplateType());
            modelMapper.map(usersViewDto, cvDto);

            return cvDto;
        } else {
            throw new IllegalArgumentException("CV not found with cvId: " + cvId);
        }
    }

    @Override
    public CvAddNewDto finishUp(int cvId) throws JsonProcessingException {
        Cv cv = cvRepository.findById(cvId).get();
        if (cv != null) {
            CvAddNewDto cvDto = cvMapper.cvAddNewDto(cv);
            UsersViewDto usersViewDto = usersMapper.toView(cv.getUser());
            modelMapper.map(usersViewDto, cvDto);
            cvDto.getCertifications().clear();
            cvDto.getExperiences().clear();
            cvDto.getInvolvements().clear();
            cvDto.getEducations().clear();
            cvDto.getProjects().clear();
            cvDto.getSourceWorks().clear();
            cvDto.getSkills().clear();
            modelMapper.map(cv.deserialize(), cvDto);

            return cvDto;
        } else {
            throw new IllegalArgumentException("CV not found with cvId: " + cvId);
        }
    }


    @Override
    public void deleteCvById(Integer UsersId, Integer id) {
        Optional<Users> UsersOptional = usersRepository.findById(UsersId);

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
    public CvAddNewDto createCv(Integer UsersId, CvBodyDto dto) throws JsonProcessingException {
        Optional<Users> UsersOptional = usersRepository.findById(UsersId);

        if (UsersOptional.isPresent()) {
            Cv cv = new Cv();
            cv.setStatus(BasicStatus.ACTIVE);
            cv.setCvBody(cv.toCvBody(dto));
            Users users = UsersOptional.get();
            cv.setUser(users);
            Cv savedCv = cvRepository.save(cv);
            UsersViewDto usersViewDto = modelMapper.map(users, UsersViewDto.class);
            CvAddNewDto response = cvMapper.cvAddNewDto(savedCv);
            CvBodyDto cvBodyDto = savedCv.deserialize();
            response.setCvStyle(cvBodyDto.getCvStyle());
            response.setTemplateType(cvBodyDto.getTemplateType());
            response.setCertifications(usersViewDto.getCertifications());
            response.setEducations(usersViewDto.getEducations());
            response.setInvolvements(usersViewDto.getInvolvements());
            response.setExperiences(usersViewDto.getExperiences());
            response.setProjects(usersViewDto.getProjects());
            response.setSkills(usersViewDto.getSkills());
            response.setSourceWorks(usersViewDto.getSourceWorks());
            return response;
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
        Optional<Users> UsersOptional = usersRepository.findById(UsersId);

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
        Optional<Users> UsersOptional = usersRepository.findById(UsersId);

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
    public UsersViewDto updateCvContact(int UsersId, UsersViewDto dto) {
        Optional<Users> usersOptional = usersRepository.findById(UsersId);

        if (usersOptional.isPresent()) {
            Users user = usersOptional.get();
            modelMapper.map(dto, user);
            user = usersRepository.save(user);
            return usersMapper.toView(user);
        } else {
            throw new IllegalArgumentException("UsersId not found: " + UsersId);
        }
    }

    @Override
    public boolean updateCvTemplate(int UsersId, int cvId, int templateId) {
        Optional<Users> UsersOptional = usersRepository.findById(UsersId);

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
        if (Objects.nonNull(cv)) {
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
