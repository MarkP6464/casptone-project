package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.SectionEvaluate;
import com.example.capstoneproject.enums.SectionLogStatus;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.mapper.CvMapper;
import com.example.capstoneproject.mapper.UsersMapper;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.*;
import com.example.capstoneproject.utils.SecurityUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CvServiceImpl implements CvService {
    @Autowired
    CvRepository cvRepository;

    @Autowired
    ReviewAiService reviewAiService;

    @Autowired
    ChatGPTServiceImpl chatGPTService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    HistorySummaryService historySummaryService;

    @Autowired
    ScoreRepository scoreRepository;

    @Autowired
    ScoreLogRepository scoreLogRepository;

    @Autowired
    SectionLogRepository sectionLogRepository;

    @Autowired
    SecurityUtil securityUtil;

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
    ExperienceService experienceService;
    @Autowired
    @Lazy
    InvolvementService involvementService;
    @Autowired
    @Lazy
    ProjectService projectService;

    @Autowired
    HistoryService historyService;

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
    ProjectRepository projectRepository;

    @Autowired
    @Lazy
    CertificationService certificationService;

    @Autowired
    EvaluateRepository evaluateRepository;

    @Autowired
    CvMapper cvMapper;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    JobDescriptionRepository jobDescriptionRepository;

    @Autowired
    AtsRepository atsRepository;

    @Autowired
    JobDescriptionService jobDescriptionService;

    public CvServiceImpl(CvRepository cvRepository, CvMapper cvMapper) {
        this.cvRepository = cvRepository;
        this.cvMapper = cvMapper;
    }

    @Override
    public List<CvViewDto> GetCvsById(Integer UsersId, String content) {
        List<Cv> cvs = cvRepository.findAllByUsersIdAndStatus(UsersId, BasicStatus.ACTIVE);
        return cvs
                .stream()
                .filter(cv -> content == null || cv.getResumeName().contains(content))
                .map(cv -> {
                    CvViewDto cvDto = new CvViewDto();
                    cvDto.setId(cv.getId());
                    cvDto.setResumeName(cv.getResumeName());
//                    cvDto.setExperience(cv.getExperience());
//                    cvDto.setFieldOrDomain(cv.getFieldOrDomain());
                    cvDto.setSummary(cv.getSummary());
                    try {
                        cvDto.setCvBody(cv.deserialize());
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException();
                    }
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
            cvDto.setTheOrder(cvBodyDto.getTheOrder());
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
//            cvDto.getCertifications().clear();
//            cvDto.getExperiences().clear();
//            cvDto.getInvolvements().clear();
//            cvDto.getEducations().clear();
//            cvDto.getProjects().clear();
//            cvDto.getSkills().clear();
            modelMapper.map(cv.deserialize(), cvDto);
            cvDto.setTheOrder(cv.deserialize().getTheOrder());
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
        Optional<Users> user = usersRepository.findById(UsersId);

        if (user.isPresent()) {
            List<Cv> cvs = cvRepository.findAllByUser_IdAndStatus(UsersId, BasicStatus.ACTIVE);
            if(cvs!=null){
                for(Cv cv:cvs){
                    if(cv.getResumeName().equals(dto.getResumeName())){
                        throw new BadRequestException("Resume name already exists in another cv.");
                    }
                }
            }
            Cv cv = new Cv();
            cv.setStatus(BasicStatus.ACTIVE);
            cv.setCompanyName(dto.getCompanyName());

            Users users = user.get();
            UsersViewDto usersViewDto = modelMapper.map(users, UsersViewDto.class);
            cv.setUser(users);

//            dto.setCertifications(usersViewDto.getCertifications());
//            List<CertificationDto> certs = dto.getCertifications();
//            List<CertificationDto> list =IntStream.range(1, certs.size()).mapToObj(index -> {
//                CertificationDto theDto = new CertificationDto();
//                theDto.setTheOrder(index);
//                theDto.setIsDisplay(false);
//                theDto.setId(certs.get(index).getId());
//                return theDto;}).collect(Collectors.toList());
//            dto.setCertifications(list);

//            dto.setEducations(usersViewDto.getEducations());
//            List<EducationDto> educationDtoList = dto.getEducations().stream().map(x -> {
//                EducationDto educationDto = new EducationDto();
//                educationDto.setIsDisplay(false);
//                educationDto.setId(x.getId());
//                return educationDto;
//            }).collect(Collectors.toList());
//            dto.setEducations(educationDtoList);
//
//            dto.setInvolvements(usersViewDto.getInvolvements());
//            List<InvolvementDto> involvementDtos = dto.getInvolvements().stream().map(x -> {
//                InvolvementDto theDto = new InvolvementDto();
//                theDto.setIsDisplay(false);
//                theDto.setId(x.getId());
//                return theDto;
//            }).collect(Collectors.toList());
//            dto.setInvolvements(involvementDtos);
//
//            dto.setExperiences(usersViewDto.getExperiences());
//            List<ExperienceDto> experienceDtos = dto.getExperiences().stream().map(x -> {
//                ExperienceDto theDto = new ExperienceDto();
//                theDto.setIsDisplay(false);
//                theDto.setId(x.getId());
//                return theDto;
//            }).collect(Collectors.toList());
//            dto.setExperiences(experienceDtos);
//
//            dto.setProjects(usersViewDto.getProjects());
//            List<ProjectDto> projectDtos = dto.getProjects().stream().map(x -> {
//                ProjectDto theDto = new ProjectDto();
//                theDto.setIsDisplay(false);
//                theDto.setId(x.getId());
//                return theDto;
//            }).collect(Collectors.toList());
//            dto.setProjects(projectDtos);
//
//            dto.setSkills(usersViewDto.getSkills());
//            List<SkillDto> skillDtos = dto.getSkills().stream().map(x -> {
//                SkillDto theDto = new SkillDto();
//                theDto.setIsDisplay(false);
//                theDto.setId(x.getId());
//                return theDto;
//            }).collect(Collectors.toList());
//            dto.setSkills(skillDtos);
//            cv.setFieldOrDomain(dto.getFieldOrDomain());
//            cv.setExperience(dto.getExperience());
            cv.setCvBody(cv.toCvBody(dto));
            cv.setResumeName(dto.getResumeName());
            cv.setSearchable(dto.getSearchable());
            cv.setSharable(dto.getSharable());
            Cv savedCv = cvRepository.save(cv);
            CvAddNewDto response = cvMapper.cvAddNewDto(savedCv);

            CvBodyDto cvBodyDto = savedCv.deserialize();
            response.setCvStyle(cvBodyDto.getCvStyle());
            response.setTemplateType(cvBodyDto.getTemplateType());
            response.setTheOrder(cvBodyDto.getTheOrder());

            if(dto.getJobTitle()!=null || dto.getJobDescription()!=null){
                JobDescription jobDescription = new JobDescription();
                jobDescription.setTitle(dto.getJobTitle());
                jobDescription.setDescription(dto.getJobDescription());
                JobDescription saved = jobDescriptionRepository.save(jobDescription);
                Integer jobId = saved.getId();
                Optional<JobDescription> jobDescriptionOptional = jobDescriptionRepository.findById(jobId);
                if(jobDescriptionOptional.isPresent()){
                    JobDescription jobDescription1 = jobDescriptionOptional.get();
                    savedCv.setJobDescription(jobDescription1);
                    cvRepository.save(savedCv);
                }
            }

            return response;
        } else {
            throw new IllegalArgumentException("Not found user with ID: " + UsersId);
        }
    }

    @Override
    public CvDto duplicateCv(Integer userId, Integer cvId) throws JsonProcessingException {
        Cv cvOfUser = cvRepository.findCvByIdAndStatus(userId, cvId, BasicStatus.ACTIVE);
        Optional<Cv> cvOptional = cvRepository.findByIdAndStatus(cvId, BasicStatus.ACTIVE);
        JobDescription newJobDescription = new JobDescription();
        CvDto cvDto = new CvDto();
        if (cvOfUser != null) {
            if (cvOptional.isPresent()) {
                Cv cv = cvOptional.get();
                CvDupDto cvDupDto = new CvDupDto();
                cvDupDto.setResumeName("Copy of " + cv.getResumeName());
//                cvDupDto.setExperience(cv.getExperience());
//                cvDupDto.setFieldOrDomain(cv.getFieldOrDomain());
                cvDupDto.setStatus(BasicStatus.ACTIVE);
                cvDupDto.setSummary(cv.getSummary());
                cvDupDto.setCvBody(cv.getCvBody());
                cvDupDto.setEvaluation(cv.getOverview());
                if (cv.getJobDescription() != null) {
                    Optional<JobDescription> jobDescriptionOptional = jobDescriptionRepository.findById(cv.getJobDescription().getId());
                    if (jobDescriptionOptional.isPresent()) {
                        JobDescriptionDto jobDescriptionDto = new JobDescriptionDto();
                        JobDescription jobDescription = jobDescriptionOptional.get();
                        jobDescriptionDto.setTitle(jobDescription.getTitle());
                        jobDescriptionDto.setDescription(jobDescription.getDescription());
                        newJobDescription = jobDescriptionRepository.save(modelMapper.map(jobDescriptionDto, JobDescription.class));
                        cvDupDto.setJobDescription(newJobDescription);
                    }
                    if (jobDescriptionOptional.isPresent()) {
                        Ats atsAdd = new Ats();
                        List<Ats> ats = atsRepository.findAllByJobDescriptionId(jobDescriptionOptional.get().getId());
                        for (Ats ats1 : ats) {
                            atsAdd.setAts(ats1.getAts());
                            atsAdd.setJobDescription(newJobDescription);
                            atsRepository.save(atsAdd);
                        }

                    }

                }
                cvDupDto.setUser(cv.getUser());
                Cv cvReturn = cvRepository.save(modelMapper.map(cvDupDto, Cv.class));
                cvDto.setId(cvReturn.getId());
                cvDto.setResumeName(cvReturn.getResumeName());
//                cvDto.setExperience(cvReturn.getExperience());
//                cvDto.setFieldOrDomain(cvReturn.getFieldOrDomain());
                cvDto.setStatus(cvReturn.getStatus());
                cvDto.setSummary(cvReturn.getSummary());
                cvDto.setCvBody(cvReturn.deserialize());
                cvDto.setEvaluate(cvReturn.getOverview() != null ? cvReturn.deserializeOverview() : null);
                cvDto.setJobDescription(cvReturn.getJobDescription());
                cvDto.setUsersDto(modelMapper.map(cvReturn.getUser(), UsersDto.class));
            } else {
                throw new RuntimeException("CV ID not found.");
            }
        } else {
            throw new RuntimeException("User ID not exist this Cv ID.");
        }
        return cvDto;
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
    public UsersCvViewDto updateCvContact(Integer userId, Integer cvId, UsersCvViewDto dto) throws JsonProcessingException {
        Optional<Cv> cvOptional = cvRepository.findByUser_IdAndId(userId,cvId);
        if(cvOptional.isPresent()){
            Cv cv = cvOptional.get();
            CvBodyDto cvBodyDto = cv.deserialize();
            cvBodyDto.setName(dto.getFullName());
            cvBodyDto.setEmail(dto.getEmail());
            cvBodyDto.setPhone(dto.getPhone());
            cvBodyDto.setLinkin(dto.getLinkin());
            cvBodyDto.setPersonalWebsite(dto.getPersonalWebsite());
            cvBodyDto.setCity(dto.getCity());
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(cvBodyDto);
            cv.setCvBody(json);
            cvRepository.save(cv);
            return dto;
        }else{
            throw new BadRequestException("User id or cv id incorrect.");
        }
    }

    @Override
    public UsersCvViewDto getCvContact(Integer userId, Integer cvId) throws JsonProcessingException {
        Optional<Users> usersOptional = usersRepository.findUsersById(userId);
        if(usersOptional.isPresent()){
            Users users = usersOptional.get();
            Optional<Cv> cvOptional = cvRepository.findByUser_IdAndId(userId,cvId);
            if(cvOptional.isPresent()){
                Cv cv = cvOptional.get();
                CvBodyDto cvBodyDto = cv.deserialize();
                UsersCvViewDto usersCvView = new UsersCvViewDto();
                if(cvBodyDto.getName()!=null){
                    usersCvView.setFullName(cvBodyDto.getName());
                }else{
                    usersCvView.setFullName(users.getName());
                }
                if(cvBodyDto.getEmail()!=null){
                    usersCvView.setEmail(cvBodyDto.getEmail());
                }else{
                    usersCvView.setEmail(users.getEmail());
                }
                if(cvBodyDto.getPhone()!=null){
                    usersCvView.setPhone(cvBodyDto.getPhone());
                }else{
                    usersCvView.setPhone(users.getPhone());
                }
                if(cvBodyDto.getLinkin()!=null){
                    usersCvView.setLinkin(cvBodyDto.getLinkin());
                }else{
                    usersCvView.setLinkin(users.getLinkin());
                }
                if(cvBodyDto.getPersonalWebsite()!=null){
                    usersCvView.setPersonalWebsite(cvBodyDto.getPersonalWebsite());
                }else{
                    usersCvView.setPersonalWebsite(users.getPersonalWebsite());
                }
                if(cvBodyDto.getCity()!=null){
                    usersCvView.setCity(cvBodyDto.getCity());
                }else{
                    usersCvView.setCity(users.getCountry());
                }
                return usersCvView;
            }else{
                throw new BadRequestException("Cv id incorrect.");
            }
        }else{
            throw new BadRequestException("User id not found.");
        }
    }

    @Override
    public Boolean updateCvTarget(Integer id, CvUpdateDto dto, Principal principal) {
        Optional<Cv> cvOptional = cvRepository.findById(id);

        if (cvOptional.isPresent()) {
            Cv cv = cvOptional.get();

            List<Cv> cvs = cvRepository.findAllByUser_IdAndIdIsNot(cv.getUser().getId(), cv.getId());
            if (cvs != null) {
                for (Cv cv1 : cvs) {
                    if (cv1.getResumeName().equals(dto.getResumeName())) {
                        throw new BadRequestException("Resume name already exists in another cv.");
                    }
                }
            }

            cv.setResumeName(dto.getResumeName());
            cvRepository.save(cv);
            return true;
        } else {
            throw new IllegalArgumentException("CvId not found: " + id);
        }
    }

    @Override
    public CvUpdateDto getTitleResume(Integer cvId) {
        Optional<Cv> cvOptional = cvRepository.findById(cvId);
        CvUpdateDto cvUpdateDto = new CvUpdateDto();
        if(cvOptional.isPresent()){
            Cv cv = cvOptional.get();
            cvUpdateDto.setResumeName(cv.getResumeName());
            return cvUpdateDto;
        }else {
            throw new BadRequestException("Cv id not found.");
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
//            Optional<Score> scoreOptional = scoreRepository.findByCv_Id(cvId);
//            if(scoreOptional.isPresent()){
//                Score score = scoreOptional.get();
//                //Delete score in db
//                scoreLogRepository.deleteAllByScore_Id(score.getId());
//
//                //Delete score in db
//                scoreRepository.deleteScoreById(score.getId());
//            }

            cv.setOverview(null);
            cvRepository.save(cv);
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
            updateCvBody(cvId, cvBodyDto);
            historyService.create(cv.getUser().getId(),cvId);
        }
        return cvMapper.mapEntityToDto(cv);
    }

    @Override
    public Cv findByUser_IdAndId(Integer UsersId, Integer cvId) {
        Optional<Cv> cvOptional = cvRepository.findByUser_IdAndId(UsersId, cvId);
        return cvOptional.orElse(null);
    }

    @Override
    public boolean searchable(Integer userId, Integer cvId) {
        Optional<Cv> cvOptional = cvRepository.findByIdAndUserId(cvId,userId);
        if(cvOptional.isPresent()){
            Cv cv = cvOptional.get();
            Optional<Cv> cvOptional1 = cvRepository.findByIdAndStatus(cv.getId(), BasicStatus.ACTIVE);
            if(cvOptional1.isPresent()){
                Cv cv1 = cvOptional1.get();
                cv1.setSearchable(true);
                cvRepository.save(cv1);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<CvAddNewDto> getListSearchable(String field) {
        List<Cv> cvs = cvRepository.findAllByStatusAndSearchable(BasicStatus.ACTIVE,true);
        return cvs.stream()
                .filter(cv -> field == null)
                .map(cv -> modelMapper.map(cv, CvAddNewDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CvResumeDto> getListResume(Integer userId) {
        List<Cv> cvs = cvRepository.findAllByUsersIdAndStatus(userId, BasicStatus.ACTIVE);
        return cvs.stream()
                .map(x -> modelMapper.map(x, CvResumeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ExperienceRoleDto> getListExperienceRole(Integer userId, Integer cvId) throws JsonProcessingException {
        Optional<Cv> cvOptional = cvRepository.findByUser_IdAndId(userId, cvId);
        List<ExperienceRoleDto> experienceRoles = new ArrayList<>();
        if(cvOptional.isPresent()){
            Cv cv = cvOptional.get();
            CvBodyDto cvBodyDto = cv.deserialize();
            experienceRoles = cvBodyDto.getExperiences().stream()
                    .filter(x -> x.getIsDisplay())
                    .map(x -> {
                        ExperienceRoleDto experienceRoleDto = new ExperienceRoleDto();
                        experienceRoleDto.setId(x.getId());
                        experienceRoleDto.setRole(x.getRole());
                        return experienceRoleDto;
                    })
                    .collect(Collectors.toList());
        }else {
            throw new RuntimeException("User ID dont have this Cv ID.");
        }
        return experienceRoles;
    }

    @Override
    public ChatResponse generateSummaryCV(Integer cvId, SummaryGenerationDto dto, Principal principal) throws JsonProcessingException {
        Optional<Cv> cvsOptional = cvRepository.findById(cvId);
        if (cvsOptional.isPresent()) {
            Cv cv = cvsOptional.get();
            CvBodyDto cvBodyDto = cv.deserialize();
            StringBuilder experienceBuilder = new StringBuilder();
            cvBodyDto.getExperiences().forEach(x -> {
                if (x.getIsDisplay()) {
                    String title = x.getRole();
                    String description = x.getDescription();
                    experienceBuilder.append(title).append("\n").append(description).append("\n");
                }
            });
            String completeSystem = "";
            String experience = "";
            if(dto.getPosition_highlight()!=null && dto.getSkill_highlight()!=null){
                completeSystem = "You are an expert in CV writing and your task is to create a resume personal statement that will be placed at the beginning of the CV. \n" +
                        "The personal statement should effectively introduce the candidate to the hiring manager and highlight why they would be a fantastic hire.\n" +
                        "The personal statement should be concise, consisting of 2-3 sentences and spanning between 30-50 words. \n" +
                        "It should begin with an attention-grabbing opening hook and clearly state the desired position as a " + dto.getPosition_highlight() + ". \n" +
                        "Soft skills and hard skills, " + dto.getSkill_highlight() + ", should be highlighted. \n" +
                        "Impressive facts and statistics should be incorporated, and the candidate’s short and long-term goals should be briefly mentioned.";
            }else if(dto.getPosition_highlight()==null && dto.getSkill_highlight()==null){
                completeSystem = "You are an expert in CV writing and your task is to create a resume personal statement that will be placed at the beginning of the CV. \n" +
                        "The personal statement should effectively introduce the candidate to the hiring manager and highlight why they would be a fantastic hire.\n" +
                        "The personal statement should be concise, consisting of 2-3 sentences and spanning between 30-50 words. \n" +
                        "It should begin with an attention-grabbing opening hook and clearly state the desired position base on the past experience. \n" +
                        "Soft skills and hard skills should be highlighted. Impressive facts and statistics should be incorporated, and the candidate’s short and long-term goals should be briefly mentioned.";
            }else if(dto.getPosition_highlight() == null){
                completeSystem = "You are an expert in CV writing and your task is to create a resume personal statement that will be placed at the beginning of the CV. \n" +
                        "The personal statement should effectively introduce the candidate to the hiring manager and highlight why they would be a fantastic hire.\n" +
                        "The personal statement should be concise, consisting of 2-3 sentences and spanning between 30-50 words. \n" +
                        "It should begin with an attention-grabbing opening hook and clearly state the desired position base on the past experience. \n" +
                        "Soft skills and hard skills, " + dto.getSkill_highlight() + ", should be highlighted. \n" +
                        "Impressive facts and statistics should be incorporated, and the candidate’s short and long-term goals should be briefly mentioned.";
            }else {
                completeSystem = "You are an expert in CV writing and your task is to create a resume personal statement that will be placed at the beginning of the CV. \n" +
                        "The personal statement should effectively introduce the candidate to the hiring manager and highlight why they would be a fantastic hire.\n" +
                        "The personal statement should be concise, consisting of 2-3 sentences and spanning between 30-50 words. \n" +
                        "It should begin with an attention-grabbing opening hook and clearly state the desired position as a " + dto.getPosition_highlight() + ". \n" +
                        "Soft skills and hard skills should be highlighted. \n" +
                        "Impressive facts and statistics should be incorporated, and the candidate’s short and long-term goals should be briefly mentioned.";
            }
            experience = experienceBuilder.toString();
            String userMessage = "Your writing will base on CV Experience:\n" + experience;
            List<Map<String, Object>> messagesList = new ArrayList<>();
            Map<String, Object> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", completeSystem);
            messagesList.add(systemMessage);
            Map<String, Object> userMessageMap = new HashMap<>();
            userMessageMap.put("role", "user");
            userMessageMap.put("content", userMessage);
            messagesList.add(userMessageMap);
            String messagesJson = new ObjectMapper().writeValueAsString(messagesList);
            transactionService.chargePerRequest(securityUtil.getLoginUser(principal).getId(), "Generate Summary");
            String response = chatGPTService.chatWithGPT(messagesJson,1);
            ChatResponse chatResponse = new ChatResponse();
            chatResponse.setReply(response);
            historySummaryService.createHistorySummary(cv.getId(), response);
            return chatResponse;
        }else{
            throw new BadRequestException("Please add experience into CV");
        }
    }

    @Override
    public ChatResponse reviewCV(float temperature, Integer cvId, Principal principal) throws JsonProcessingException {
        Optional<Cv> cvsOptional = cvRepository.findById(cvId);
        if (cvsOptional.isPresent()) {
            Cv cv = cvsOptional.get();
            CvBodyDto cvBodyDto = cv.deserialize();
            final boolean[] addedCertificationLabel = {false};
            final boolean[] addedEducationLabel = {false};
            final boolean[] addedSkillLabel = {false};
            final boolean[] addedInvolvementLabel = {false};
            final boolean[] addedProjectLabel = {false};
            final boolean[] addedExperienceLabel = {false};
            StringBuilder experienceBuilder = new StringBuilder();

            if(cv.getUser().getName()!=null){
                experienceBuilder.append(cv.getUser().getName()).append("\n");
            }
            if(cv.getUser().getEmail()!=null){
                experienceBuilder.append(cv.getUser().getEmail()).append("\n");
            }
            if(cv.getUser().getPhone()!=null){
                experienceBuilder.append(cv.getUser().getPhone()).append("\n");
            }
            if(cv.getUser().getLinkin()!=null){
                experienceBuilder.append(cv.getUser().getLinkin()).append("\n");
            }
            if(cv.getSummary()!=null){
                experienceBuilder.append("SUMMARY").append("\n");
                experienceBuilder.append(cv.getSummary()).append("\n");
            }

            List<Object> displayItems = new ArrayList<>();

            List<ExperienceDto> displayExperiences = cvBodyDto.getExperiences().stream()
                    .filter(ExperienceDto::getIsDisplay)
                    .collect(Collectors.toList());

            displayItems.addAll(displayExperiences);

            List<ProjectDto> displayProjects = cvBodyDto.getProjects().stream()
                    .filter(ProjectDto::getIsDisplay)
                    .collect(Collectors.toList());

            displayItems.addAll(displayProjects);

            List<InvolvementDto> displayInvolvements = cvBodyDto.getInvolvements().stream()
                    .filter(InvolvementDto::getIsDisplay)
                    .collect(Collectors.toList());

            displayItems.addAll(displayInvolvements);

            List<EducationDto> displayEducations = cvBodyDto.getEducations().stream()
                    .filter(EducationDto::getIsDisplay)
                    .collect(Collectors.toList());

            displayItems.addAll(displayEducations);

            List<CertificationDto> displayCertifications = cvBodyDto.getCertifications().stream()
                    .filter(CertificationDto::getIsDisplay)
                    .collect(Collectors.toList());

            displayItems.addAll(displayCertifications);

            List<SkillDto> displaySkills = cvBodyDto.getSkills().stream()
                    .filter(SkillDto::getIsDisplay)
                    .collect(Collectors.toList());

            displayItems.addAll(displaySkills);

            // Lặp qua danh sách đã sắp xếp và xử lý theo cách bạn muốn
            displayItems.forEach(item -> {
                if (item instanceof ExperienceDto) {
                    if (!addedExperienceLabel[0]) {
                        experienceBuilder.append("EXPERIENCE").append("\n");
                        addedExperienceLabel[0] = true;
                    }
                    ExperienceDto experience = (ExperienceDto) item;
                    String title = experience.getRole();
                    String company = experience.getCompanyName();
                    String duration = experience.getDuration();
                    String location = experience.getLocation();
                    String description = experience.getDescription();
                    if (title != null) {
                        experienceBuilder.append(title).append("\n");
                    }
                    if (company != null) {
                        experienceBuilder.append(company).append("\n");
                    }
                    if (duration != null) {
                        experienceBuilder.append(duration).append("\n");
                    }
                    if (location != null) {
                        experienceBuilder.append(location).append("\n");
                    }
                    if (description != null) {
                        experienceBuilder.append(description).append("\n");
                    }
                } else if (item instanceof ProjectDto) {
                    if (!addedProjectLabel[0]) {
                        experienceBuilder.append("PROJECT").append("\n");
                        addedProjectLabel[0] = true;
                    }
                    ProjectDto project = (ProjectDto) item;
                    String title = project.getTitle();
                    String organization = project.getOrganization();
                    String duration = project.getOrganization();
                    String description = project.getDescription();
                    if (title != null) {
                        experienceBuilder.append(title).append("\n");
                    }
                    if (organization != null) {
                        experienceBuilder.append(organization).append("\n");
                    }
                    if (duration != null) {
                        experienceBuilder.append(duration).append("\n");
                    }
                    if (description != null) {
                        experienceBuilder.append(description).append("\n");
                    }
                } else if (item instanceof InvolvementDto) {
                    if (!addedInvolvementLabel[0]) {
                        experienceBuilder.append("INVOLVEMENT").append("\n");
                        addedInvolvementLabel[0] = true;
                    }
                    InvolvementDto involvement = (InvolvementDto) item;
                    String title = involvement.getOrganizationRole();
                    String name = involvement.getOrganizationName();
                    String duration = involvement.getDuration();
                    String col = involvement.getCollege();
                    String description = involvement.getDescription();
                    if (title != null) {
                        experienceBuilder.append(title).append("\n");
                    }
                    if (name != null) {
                        experienceBuilder.append(name).append("\n");
                    }
                    if (duration != null) {
                        experienceBuilder.append(duration).append("\n");
                    }
                    if (col != null) {
                        experienceBuilder.append(col).append("\n");
                    }
                    if (description != null) {
                        experienceBuilder.append(description).append("\n");
                    }
                } else if (item instanceof EducationDto) {
                    if (!addedEducationLabel[0]) {
                        experienceBuilder.append("EDUCATION").append("\n");
                        addedEducationLabel[0] = true;
                    }
                    EducationDto education = (EducationDto) item;
                    String title = education.getDegree();
                    String relation = education.getCollegeName();
                    String location = education.getLocation();
                    String description = education.getDescription();
                    if (title != null) {
                        experienceBuilder.append(title).append("\n");
                    }
                    if (relation != null) {
                        experienceBuilder.append(relation).append("\n");
                    }
                    if (location != null) {
                        experienceBuilder.append(location).append("\n");
                    }
                    if (description != null) {
                        experienceBuilder.append(description).append("\n");
                    }
                } else if (item instanceof CertificationDto) {
                    if (!addedCertificationLabel[0]) {
                        experienceBuilder.append("CERTIFICATION").append("\n");
                        addedCertificationLabel[0] = true;
                    }
                    CertificationDto certification = (CertificationDto) item;
                    String title = certification.getName();
                    String location = certification.getCertificateSource();
                    String relative = certification.getCertificateRelevance();
                    String description = certification.getCertificateRelevance();
                    if (title != null) {
                        experienceBuilder.append(title).append("\n");
                    }
                    if (location != null) {
                        experienceBuilder.append(location).append("\n");
                    }
                    if (relative != null) {
                        experienceBuilder.append(relative).append("\n");
                    }
                    if (description != null) {
                        experienceBuilder.append(description).append("\n");
                    }
                } else if (item instanceof SkillDto) {
                    if (!addedSkillLabel[0]) {
                        experienceBuilder.append("SKILL").append("\n");
                        addedSkillLabel[0] = true;
                    }
                    SkillDto skill = (SkillDto) item;
                    String description = skill.getDescription();
                    if (description != null) {
                        experienceBuilder.append(description).append("\n");
                    }
                }
            });

            String completeSystem = "Please provide detailed feedback on the content of my CV. Specifically, evaluate the following aspects: \n" +
                    "Clarity: Is the information presented in a clear and concise manner? Can you easily understand my qualifications and experiences? \n" +
                    "Relevance: Are the details included in my CV relevant to the job or field I am applying for? Are there any sections that could be omitted or added for better alignment with the position?\n" +
                    "Accomplishments: Do my achievements and accomplishments stand out? Are they presented with specific, quantifiable results whenever possible? \n" +
                    "Structure: Is the overall structure of my CV effective? Does it have a clear, logical flow? Is there a clear hierarchy of information, such as personal details, summary, work experience, education, skills, etc.? \n" +
                    "Grammar and Spelling: Are there any grammatical or spelling errors that need correction? \n" +
                    "Formatting: Is the formatting consistent and visually appealing? Does the CV use an easy-to-read font and style? \n" +
                    "Keywords: Have I included relevant keywords that align with the job I am targeting? Are there industry-specific terms that I should incorporate? \n" +
                    "Length: Is the CV an appropriate length, not too long or too short for the industry and position I'm applying for? \n" +
                    "Personal Statement/Objective: Does my personal statement or objective effectively introduce me to potential employers and convey my career goals? \n" +
                    "Additional Sections: Are there any additional sections or information that could enhance my CV, such as certifications, volunteer work, or hobbies, depending on the job or field? \n" +
                    "Please provide specific feedback and suggestions for improvement in each of these areas. Your insights will be greatly appreciated as I work to make my CV as strong as possible. \n" +
                    "Thank you! \n";
            String userMessage = "Here is my CV:\n" + experienceBuilder;
            List<Map<String, Object>> messagesList = new ArrayList<>();
            Map<String, Object> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", completeSystem);
            messagesList.add(systemMessage);
            Map<String, Object> userMessageMap = new HashMap<>();
            userMessageMap.put("role", "user");
            userMessageMap.put("content", userMessage);
            messagesList.add(userMessageMap);
            String messagesJson = new ObjectMapper().writeValueAsString(messagesList);
            transactionService.chargePerRequest(securityUtil.getLoginUser(principal).getId(), "AI feedback");
            String response = chatGPTService.chatWithGPTCoverLetter(messagesJson,temperature);
            ReviewAiDto reviewAiDto = new ReviewAiDto();
            reviewAiDto.setReview(response);
            reviewAiService.createReviewAi(cvId,reviewAiDto);
            ChatResponse chatResponse = new ChatResponse();
            chatResponse.setReply(response);
            return chatResponse;
        }else{
            throw new BadRequestException("Please add experience into CV");
        }
    }

    @Override
    public ChatResponseArray rewritteExperience(ReWritterExperienceDto dto, Principal principal) throws JsonProcessingException {
        if(dto.getJobTitle()!=null && dto.getBullet()!=null){
            String system = "Improve writing prompt\n" +
                    "As an expert in CV writing, your task is to enhance the description of experience as a " + dto.getJobTitle() + ". The revised writing should keep the original content and adhere to best practices in CV writing, including short, concise bullet points, quantify if possible , focusing on achievements rather than responsibilities. Your response solely provide the content base on the current description provided:";
            String userMessage = "“" + dto.getBullet() + "”\n" +
                    "Start with bullet point don’t need to elaborate any unnecessary information";
            ChatResponseArray chatResponse = new ChatResponseArray();
            List<Map<String, Object>> messagesList = new ArrayList<>();
            Map<String, Object> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", system);
            messagesList.add(systemMessage);
            Map<String, Object> userMessageMap = new HashMap<>();
            userMessageMap.put("role", "user");
            userMessageMap.put("content", userMessage);
            messagesList.add(userMessageMap);
            String messagesJson = new ObjectMapper().writeValueAsString(messagesList);
            transactionService.chargePerRequest(securityUtil.getLoginUser(principal).getId(), "Improving writing");
            String response = chatGPTService.chatWithGPTCoverLetterRevise(messagesJson);
            chatResponse.setReply(splitText(response));
            return chatResponse;
        }else{
            throw new BadRequestException("Please enter full job title and description.");
        }
    }

    @Override
    public List<CvResponse> listCvDetail(Integer userId) {
        List<Cv> cvs = cvRepository.findAllByUser_IdAndStatus(userId, BasicStatus.ACTIVE);
        List<CvResponse> list = new ArrayList<>();
        if(cvs!=null){
            for(Cv cv:cvs){
                CvResponse cvResponse = new CvResponse();
                cvResponse.setId(cv.getId());
                cvResponse.setResume(cv.getResumeName());
                cvResponse.setCompany(cv.getCompanyName());
                if (cv.getJobDescription() != null) {
                    cvResponse.setJobTitle(cv.getJobDescription().getTitle());
                    cvResponse.setJobDescription(cv.getJobDescription().getDescription());
                }
                list.add(cvResponse);
            }
        }
        return list;
    }

    @Override
    public ResumeTitleResponse getResumeName(Integer cvId) {
        Optional<Cv> cvOptional = cvRepository.findByIdAndStatus(cvId,BasicStatus.ACTIVE);
        if(cvOptional.isPresent()){
            Cv cv = cvOptional.get();
            ResumeTitleResponse response = new ResumeTitleResponse();
            response.setResume(cv.getResumeName());
            return response;
        }else{
            throw new BadRequestException("Cv id not found.");
        }
    }

    public static String[] splitText(String text) {
        // Use positive lookahead to include "• " in the split result
        String[] splitValues = text.split("(?=[•\\-])");

        // Trim and filter out empty values, replace "-" with "•"
        return Arrays.stream(splitValues)
                .map(String::trim)
                .filter(value -> !value.isEmpty())
                .map(value -> value.replace("-", "•"))
                .toArray(String[]::new);
    }

    public static int countWords(String input) {
        if (input == null || input.isEmpty()) {
            return 0;
        }

        // Sử dụng biểu thức chính quy để tách các từ
        String[] words = input.split("\\s+");

        // Trả về số lượng từ
        return words.length;
    }
}
