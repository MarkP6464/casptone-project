package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.CvViewDto;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.SectionEvaluate;
import com.example.capstoneproject.enums.SectionLogStatus;
import com.example.capstoneproject.mapper.CvMapper;
import com.example.capstoneproject.mapper.UsersMapper;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CvServiceImpl implements CvService {
    @Autowired
    CvRepository cvRepository;

    @Autowired
    ScoreRepository scoreRepository;

    @Autowired
    ScoreLogRepository scoreLogRepository;

    @Autowired
    SectionLogRepository sectionLogRepository;

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
                    cvDto.setExperience(cv.getExperience());
                    cvDto.setFieldOrDomain(cv.getFieldOrDomain());
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
        Optional<Users> user = usersRepository.findById(UsersId);

        if (user.isPresent()) {
            Cv cv = new Cv();
            cv.setStatus(BasicStatus.ACTIVE);

            Users users = user.get();
            UsersViewDto usersViewDto = modelMapper.map(users, UsersViewDto.class);
            cv.setUser(users);

            dto.setCertifications(usersViewDto.getCertifications());
            List<CertificationDto> list = dto.getCertifications().stream().map(x -> {
                CertificationDto theDto = new CertificationDto();
                theDto.setIsDisplay(false);
                theDto.setId(x.getId());
                return theDto;
            }).collect(Collectors.toList());
            dto.setCertifications(list);

            dto.setEducations(usersViewDto.getEducations());
            List<EducationDto> educationDtoList = dto.getEducations().stream().map(x -> {
                EducationDto educationDto = new EducationDto();
                educationDto.setIsDisplay(false);
                educationDto.setId(x.getId());
                return educationDto;
            }).collect(Collectors.toList());
            dto.setEducations(educationDtoList);

            dto.setInvolvements(usersViewDto.getInvolvements());
            List<InvolvementDto> involvementDtos = dto.getInvolvements().stream().map(x -> {
                InvolvementDto theDto = new InvolvementDto();
                theDto.setIsDisplay(false);
                theDto.setId(x.getId());
                return theDto;
            }).collect(Collectors.toList());
            dto.setInvolvements(involvementDtos);

            dto.setExperiences(usersViewDto.getExperiences());
            List<ExperienceDto> experienceDtos = dto.getExperiences().stream().map(x -> {
                ExperienceDto theDto = new ExperienceDto();
                theDto.setIsDisplay(false);
                theDto.setId(x.getId());
                return theDto;
            }).collect(Collectors.toList());
            dto.setExperiences(experienceDtos);

            dto.setProjects(usersViewDto.getProjects());
            List<ProjectDto> projectDtos = dto.getProjects().stream().map(x -> {
                ProjectDto theDto = new ProjectDto();
                theDto.setIsDisplay(false);
                theDto.setId(x.getId());
                return theDto;
            }).collect(Collectors.toList());
            dto.setProjects(projectDtos);

            dto.setSkills(usersViewDto.getSkills());
            List<SkillDto> skillDtos = dto.getSkills().stream().map(x -> {
                SkillDto theDto = new SkillDto();
                theDto.setIsDisplay(false);
                theDto.setId(x.getId());
                return theDto;
            }).collect(Collectors.toList());
            dto.setSkills(skillDtos);

            cv.setCvBody(cv.toCvBody(dto));
            cv.setFieldOrDomain(dto.getFieldOrDomain());
            cv.setResumeName(dto.getResumeName());
            cv.setExperience(dto.getExperience());
            cv.setSearchable(dto.getSearchable());
            cv.setSharable(dto.getSharable());
            Cv savedCv = cvRepository.save(cv);
            CvAddNewDto response = cvMapper.cvAddNewDto(savedCv);

            CvBodyDto cvBodyDto = savedCv.deserialize();
            response.setCvStyle(cvBodyDto.getCvStyle());
            response.setTemplateType(cvBodyDto.getTemplateType());

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
                cvDupDto.setExperience(cv.getExperience());
                cvDupDto.setFieldOrDomain(cv.getFieldOrDomain());
                cvDupDto.setStatus(BasicStatus.ACTIVE);
                cvDupDto.setSummary(cv.getSummary());
                cvDupDto.setCvBody(cv.getCvBody());
                cvDupDto.setEvaluation(cv.getEvaluation());
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
                cvDto.setExperience(cvReturn.getExperience());
                cvDto.setFieldOrDomain(cvReturn.getFieldOrDomain());
                cvDto.setStatus(cvReturn.getStatus());
                cvDto.setSummary(cvReturn.getSummary());
                cvDto.setCvBody(cvReturn.deserialize());
                cvDto.setEvaluate(cvReturn.getEvaluation() != null ? cvReturn.deserializeScore() : null);
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
            updateCvBody(cvId, cvBodyDto);
            historyService.create(cv.getUser().getId(),cvId);
        }
        return cvMapper.mapEntityToDto(cv);
    }

    @Override
    public List<ScoreDto> getEvaluateCv(int userId, int cvId) throws JsonProcessingException {
        Cv cv = cvRepository.getById(cvId);
        List<SectionCvDto> sectionCvDtos = new ArrayList<>();
        List<SectionAddScoreLogDto> sectionAddScoreLogDtos = new ArrayList<>();
        List<Evaluate> evaluates = evaluateRepository.findAll();
        Optional<Score> scoreOptional = scoreRepository.findByCv_Id(cvId);
        if(scoreOptional.isPresent()){
            Score score = scoreOptional.get();

            final int[] totalWords = {0};

            if (Objects.nonNull(cv)) {
                CvBodyDto cvBodyDto = cv.deserialize();
                List<ContentDto> contentList = new ArrayList<>();
                List<ContentDto> practiceList = new ArrayList<>();
                List<ContentDto> optimizationList = new ArrayList<>();
                for (SkillDto x : cvBodyDto.getSkills()) {
                    if (x.getIsDisplay()) {
                        String description = x.getDescription();
                        if (description != null) {
                            String[] words = description.split("\\s+");
                            totalWords[0] += words.length;
                        }
                    }
                }

                cvBodyDto.getCertifications().forEach(x -> {
                    if (x.getIsDisplay()) {
                        String skill = x.getCertificateRelevance();
                        if (skill != null) {
                            String word = skill;
                            String[] words = word.split("\\s+");
                            totalWords[0] += words.length;
                        }
                    }
                });

                cvBodyDto.getEducations().forEach(x -> {
                    if(x.getIsDisplay()){
                        String minor = x.getMinor();
                        String description = x.getDescription();
                        if(minor==null){
                            minor = "";
                        }
                        if(description==null){
                            description = "";
                        }
                        String word = description + " " + minor;
                        String[] words = word.split("\\s+");
                        totalWords[0] += words.length;
                    }
                });

                cvBodyDto.getExperiences().forEach(x -> {
                    if(x.getIsDisplay()){
                        int experienceId = x.getId();
                        String title = x.getRole();
                        String location = x.getLocation();
                        String duration = x.getDuration();
                        String company = x.getCompanyName();
                        String description = x.getDescription();
                        if(title==null){
                            title = "";
                        }
                        if(location==null){
                            location = "";
                        }
                        if(duration==null){
                            duration = "";
                        }
                        if(company==null){
                            company = "";
                        }
                        if(description==null){
                            description = "";
                        }
                        String word = title + " " + location + " " + company + " " + description;
                        String[] words = word.split("\\s+");
                        totalWords[0] += words.length;
                        sectionCvDtos.add(new SectionCvDto(SectionEvaluate.experience, experienceId, title, location, duration));
                        sectionAddScoreLogDtos.add(new SectionAddScoreLogDto(SectionEvaluate.experience, experienceId));
                        Experience e = experienceRepository.findById(x.getId().intValue()).get();
                        modelMapper.map(e, x);
                    }
                });

                cvBodyDto.getProjects().forEach(x -> {
                    if(x.getIsDisplay()){
                        int projectId = x.getId();
                        String title = x.getTitle();
                        String duration = x.getDuration();
                        String organization = x.getOrganization();
                        String description = x.getDescription();
                        if(title==null){
                            title = "";
                        }
                        if(organization==null){
                            organization = "";
                        }
                        if(duration==null){
                            duration = "";
                        }
                        if(description==null){
                            description = "";
                        }
                        String word = title + " " + organization + " " + description;
                        String[] words = word.split("\\s+");
                        totalWords[0] += words.length;
                        sectionCvDtos.add(new SectionCvDto(SectionEvaluate.project, projectId, title, null, duration));
                        sectionAddScoreLogDtos.add(new SectionAddScoreLogDto(SectionEvaluate.project, projectId));
                        Project e = projectRepository.findById(x.getId().intValue()).get();
                        modelMapper.map(e, x);
                    }
                });

                cvBodyDto.getInvolvements().forEach(x -> {
                    if(x.getIsDisplay()){
                        int involvementId = x.getId();
                        String duration = x.getDuration();
                        String title = x.getOrganizationRole();
                        String name = x.getOrganizationName();
                        String college = x.getCollege();
                        String description = x.getDescription();
                        if(title==null){
                            title = "";
                        }
                        if(name==null){
                            name = "";
                        }
                        if(duration==null){
                            duration = "";
                        }
                        if(college==null){
                            college = "";
                        }
                        if(description==null){
                            description = "";
                        }
                        String word = title + " " + name + " " + college + " " + description;
                        String[] words = word.split("\\s+");
                        totalWords[0] += words.length;
                        sectionCvDtos.add(new SectionCvDto(SectionEvaluate.involvement, involvementId, title, null, duration));
                        sectionAddScoreLogDtos.add(new SectionAddScoreLogDto(SectionEvaluate.involvement, involvementId));
                        Involvement e = involvementRepository.findById(x.getId().intValue()).get();
                        modelMapper.map(e, x);
                    }
                });

                contentList = evaluateContentSections(evaluates, sectionCvDtos);

                practiceList = evaluateBestPractices(evaluates, sectionCvDtos, userId, cvId, totalWords, cv);

                optimizationList = evaluateOptimational(evaluates, cvId);

                ScoreDto scoreDto = new ScoreDto(score.getContent(),contentList,score.getPractice(), practiceList,score.getOptimization(), optimizationList,score.getResult());
                List<ScoreDto> result = new ArrayList<>();
                result.add(scoreDto);

                return result;
            }
        }else{
            Score score = new Score();
            score.setCv(cv);
            Score savedScore = scoreRepository.save(score);

            Integer savedScoreId = savedScore.getId();

            final int[] totalWords = {0};

            if (Objects.nonNull(cv)) {
                CvBodyDto cvBodyDto = cv.deserialize();
                List<ContentDto> contentList = new ArrayList<>();
                List<ContentDto> practiceList = new ArrayList<>();
                List<ContentDto> optimizationList = new ArrayList<>();
                for (SkillDto x : cvBodyDto.getSkills()) {
                    if (x.getIsDisplay()) {
                        String description = x.getDescription();
                        if (description != null) {
                            String[] words = description.split("\\s+");
                            totalWords[0] += words.length;
                        }
                    }
                }

                cvBodyDto.getCertifications().forEach(x -> {
                    if (x.getIsDisplay()) {
                        String skill = x.getCertificateRelevance();
                        if (skill != null) {
                            String word = skill;
                            String[] words = word.split("\\s+");
                            totalWords[0] += words.length;
                        }
                    }
                });

                cvBodyDto.getEducations().forEach(x -> {
                    if(x.getIsDisplay()){
                        String minor = x.getMinor();
                        String description = x.getDescription();
                        if(minor==null){
                            minor = "";
                        }
                        if(description==null){
                            description = "";
                        }
                        String word = description + " " + minor;
                        String[] words = word.split("\\s+");
                        totalWords[0] += words.length;
                    }
                });

                cvBodyDto.getExperiences().forEach(x -> {
                    if(x.getIsDisplay()){
                        int experienceId = x.getId();
                        String title = x.getRole();
                        String location = x.getLocation();
                        String duration = x.getDuration();
                        String company = x.getCompanyName();
                        String description = x.getDescription();
                        if(title==null){
                            title = "";
                        }
                        if(location==null){
                            location = "";
                        }
                        if(duration==null){
                            duration = "";
                        }
                        if(company==null){
                            company = "";
                        }
                        if(description==null){
                            description = "";
                        }
                        String word = title + " " + location + " " + company + " " + description;
                        String[] words = word.split("\\s+");
                        totalWords[0] += words.length;
                        sectionCvDtos.add(new SectionCvDto(SectionEvaluate.experience, experienceId, title, location, duration));
                        sectionAddScoreLogDtos.add(new SectionAddScoreLogDto(SectionEvaluate.experience, experienceId));
                        Experience e = experienceRepository.findById(x.getId().intValue()).get();
                        modelMapper.map(e, x);
                    }
                });

                cvBodyDto.getProjects().forEach(x -> {
                    if(x.getIsDisplay()){
                        int projectId = x.getId();
                        String title = x.getTitle();
                        String duration = x.getDuration();
                        String organization = x.getOrganization();
                        String description = x.getDescription();
                        if(title==null){
                            title = "";
                        }
                        if(organization==null){
                            organization = "";
                        }
                        if(duration==null){
                            duration = "";
                        }
                        if(description==null){
                            description = "";
                        }
                        String word = title + " " + organization + " " + description;
                        String[] words = word.split("\\s+");
                        totalWords[0] += words.length;
                        sectionCvDtos.add(new SectionCvDto(SectionEvaluate.project, projectId, title, null, duration));
                        sectionAddScoreLogDtos.add(new SectionAddScoreLogDto(SectionEvaluate.project, projectId));
                        Project e = projectRepository.findById(x.getId().intValue()).get();
                        modelMapper.map(e, x);
                    }
                });

                cvBodyDto.getInvolvements().forEach(x -> {
                    if(x.getIsDisplay()){
                        int involvementId = x.getId();
                        String duration = x.getDuration();
                        String title = x.getOrganizationRole();
                        String name = x.getOrganizationName();
                        String college = x.getCollege();
                        String description = x.getDescription();
                        if(title==null){
                            title = "";
                        }
                        if(name==null){
                            name = "";
                        }
                        if(duration==null){
                            duration = "";
                        }
                        if(college==null){
                            college = "";
                        }
                        if(description==null){
                            description = "";
                        }
                        String word = title + " " + name + " " + college + " " + description;
                        String[] words = word.split("\\s+");
                        totalWords[0] += words.length;
                        sectionCvDtos.add(new SectionCvDto(SectionEvaluate.involvement, involvementId, title, null, duration));
                        sectionAddScoreLogDtos.add(new SectionAddScoreLogDto(SectionEvaluate.involvement, involvementId));
                        Involvement e = involvementRepository.findById(x.getId().intValue()).get();
                        modelMapper.map(e, x);
                    }
                });

                for (SectionAddScoreLogDto sectionLog1: sectionAddScoreLogDtos){
                    List<SectionLog> sectionLog = sectionLogRepository.findBySection_TypeNameAndSection_TypeId(sectionLog1.getTypeName(), sectionLog1.getTypeId());
                    for(SectionLog sectionLog2: sectionLog){
                        ScoreLog scoreLog = new ScoreLog();
                        scoreLog.setSectionLog(sectionLog2);
                        scoreLog.setScore(savedScore);
                        scoreLogRepository.save(scoreLog);
                    }
                }
                Double content = cvRepository.calculateTotalScoreByScoreIdAndStatus(savedScoreId,SectionLogStatus.Pass);
                Double practice = evaluateBestPracticesScore(sectionCvDtos, userId, cvId, totalWords);
                Double optimization = evaluateOptimationalScore(evaluates, cvId);
                Double totalScore = calculateTotalScore(evaluates);
                double resultPercentage = (content / totalScore) * 40;
                double practicePercentage = (practice / totalScore) * 30;
                double optimizationPercentage = (optimization / totalScore) * 30;
                double totalPercentage = resultPercentage + practicePercentage + optimizationPercentage;
                String resultLabel = getResultLabel(totalPercentage);

                Score scoreOptional1 = scoreRepository.findById1(savedScoreId);
                scoreOptional1.setContent((int) Math.round(content));
                scoreOptional1.setPractice((int) Math.round(practice));
                scoreOptional1.setOptimization((int) Math.round(optimization));
                scoreOptional1.setResult(resultLabel);
                scoreRepository.save(scoreOptional1);

                contentList = evaluateContentSections(evaluates, sectionCvDtos);

                practiceList = evaluateBestPractices(evaluates, sectionCvDtos, userId, cvId, totalWords, cv);

                optimizationList = evaluateOptimational(evaluates, cvId);

                ScoreDto scoreDto = new ScoreDto((int) Math.round(content),contentList,(int) Math.round(practice), practiceList,(int) Math.round(optimization), optimizationList,resultLabel);
                List<ScoreDto> result = new ArrayList<>();
                result.add(scoreDto);

                return result;
            }

        }
        return Collections.emptyList();
    }

    public static double calculateTotalScore(List<Evaluate> evaluates) {
        return evaluates.stream()
                .mapToDouble(Evaluate::getScore)
                .sum();
    }

    private String getResultLabel(double percentage) {
        if (percentage < 50) {
            return "bad";
        } else if (percentage >= 50  && percentage <= 79) {
            return "improvement";
        } else {
            return "excellent";
        }
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
                .filter(cv -> field == null || cv.getFieldOrDomain().contains(field))
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

    private List<ContentDto> evaluateContentSections(List<Evaluate> evaluates, List<SectionCvDto> sectionCvDtos) {
        List<ContentDto> contentList = new ArrayList<>();
        int evaluateId = 1;

        for (int i = 1; i <= 8; i++) {
            Evaluate evaluate = evaluates.get(i - 1);
            List<Section> sections = cvRepository.findSectionsWithNonPassStatus(evaluateId, SectionLogStatus.Pass);

            List<ContentDetailDto> sameSections = findSameSections(sectionCvDtos, sections);

            if (!sameSections.isEmpty()) {
                ContentDto contentDto = new ContentDto(evaluate.getTitle(), evaluate.getDescription(), sameSections);
                contentList.add(contentDto);
            }

            evaluateId++;
        }

        return contentList;
    }

    private List<ContentDetailDto> findSameSections(List<SectionCvDto> sectionCvDtos, List<Section> sections) {
        List<ContentDetailDto> sameSections = new ArrayList<>();

        for (SectionCvDto sectionCvDto : sectionCvDtos) {
            for (Section section : sections) {
                if (section.getTypeName() == sectionCvDto.getTypeName() && section.getTypeId() == sectionCvDto.getTypeId()
                        && sectionCvDto.getTitle() != null) {
                    sameSections.add(new ContentDetailDto(sectionCvDto.getTypeName(), sectionCvDto.getTypeId(), sectionCvDto.getTitle()));
                }
            }
        }

        return sameSections;
    }

    private List<ContentDto> evaluateBestPractices(List<Evaluate> evaluates, List<SectionCvDto> sectionCvDtos, int userId, int cvId, int[] totalWords, Cv cv) {
        List<ContentDto> resultList = new ArrayList<>();

        for (int i = 8; i < 14; i++) {
            Evaluate evaluate = evaluates.get(i);
            List<ContentDetailDto> sameSections = new ArrayList<>();

            switch (i) {
                case 8:
                    // Check location = null
                    List<Section> sections = cvRepository.findAllByTypeName(SectionEvaluate.experience);
                    for (SectionCvDto sectionCvDto : sectionCvDtos) {
                        for (Section section : sections) {
                            if (section.getTypeName() == sectionCvDto.getTypeName() && section.getTypeId() == sectionCvDto.getTypeId()
                                    && section.getTypeName() == SectionEvaluate.experience && sectionCvDto.getLocation() == null
                                    && sectionCvDto.getTitle() != null) {
                                sameSections.add(new ContentDetailDto(sectionCvDto.getTypeName(), sectionCvDto.getTypeId(), sectionCvDto.getTitle()));
                                ContentDto contentDto = new ContentDto(evaluate.getTitle(), evaluate.getDescription(), sameSections);
                                resultList.add(contentDto);
                            }
                        }
                    }
                    break;

                case 9:
                    // Check date = null
                    List<Section> dateSections = cvRepository.findAllByTypeNames(Arrays.asList(SectionEvaluate.experience, SectionEvaluate.project, SectionEvaluate.involvement));
                    for (SectionCvDto sectionCvDto : sectionCvDtos) {
                        for (Section section : dateSections) {
                            if (section.getTypeName() == sectionCvDto.getTypeName() && section.getTypeId() == sectionCvDto.getTypeId()
                                    && (sectionCvDto.getDuration() == null && sectionCvDto.getTitle() != null)) {
                                sameSections.add(new ContentDetailDto(sectionCvDto.getTypeName(), sectionCvDto.getTypeId(), sectionCvDto.getTitle()));
                                ContentDto contentDto = new ContentDto(evaluate.getTitle(), evaluate.getDescription(), sameSections);
                                resultList.add(contentDto);
                            }
                        }
                    }
                    break;

                case 10:
                    // Check phone = null
                    Optional<Users> users = usersRepository.findUsersById(userId);
                    if (users.isPresent() && users.get().getPhone() == null) {
                        ContentDto contentDto = new ContentDto(evaluate.getTitle(), evaluate.getDescription(), null);
                        resultList.add(contentDto);
                        // Do something
                    }
                    break;

                case 11:
                    // Check linkin = null
                    Optional<Users> users1 = usersRepository.findUsersById(userId);
                    if (users1.isPresent() && users1.get().getLinkin() == null) {
                        // Do something
                        ContentDto contentDto = new ContentDto(evaluate.getTitle(), evaluate.getDescription(), null);
                        resultList.add(contentDto);
                    }
                    break;

                case 12:
                    // Check count word
                    if (totalWords[0] < 300) {
                        String totalWordsString = Arrays.toString(totalWords);
                        ContentDto contentDto = new ContentDto(evaluate.getTitle(), evaluate.getDescription() + totalWordsString, null);
                        resultList.add(contentDto);
                    }
                    break;

                case 13:
                    // Check summary
                    Cv cv1 = cvRepository.findCvById(cvId, BasicStatus.ACTIVE);
                    if (cv1.getSummary() == null || cv1.getSummary().length() < 30) {
                        ContentDto contentDto = new ContentDto(evaluate.getTitle(), evaluate.getDescription(), null);
                        resultList.add(contentDto);
                    }
                    break;

                default:
                    break;
            }
        }
        return resultList;
    }

    private Double evaluateBestPracticesScore(List<SectionCvDto> sectionCvDtos, int userId, int cvId, int[] totalWords) {
        double scorePractice = 0.0;
        for (int i = 8; i < 14; i++) {
            switch (i) {
                case 8:
                    // Check location = null
                    List<Section> sections = cvRepository.findAllByTypeName(SectionEvaluate.experience);
                    boolean atLeastOneMatch = false;
                    for (SectionCvDto sectionCvDto : sectionCvDtos) {
                        for (Section section : sections) {
                            if (section.getTypeName() == sectionCvDto.getTypeName() && section.getTypeId() == sectionCvDto.getTypeId()
                                    && section.getTypeName() == SectionEvaluate.experience && sectionCvDto.getLocation() == null
                                    && sectionCvDto.getTitle() != null) {
                                atLeastOneMatch = true;
                            }
                        }
                    }
                    if (!atLeastOneMatch) {
                        Evaluate evaluate1 = evaluateRepository.findById(9);
                        if(evaluate1!=null){
                            scorePractice += evaluate1.getScore();
                        }
                    }
                    break;

                case 9:
                    // Check date = null
                    List<Section> dateSections = cvRepository.findAllByTypeNames(Arrays.asList(SectionEvaluate.experience, SectionEvaluate.project, SectionEvaluate.involvement));
                    boolean atLeastOneMatchdate = false;
                    for (SectionCvDto sectionCvDto : sectionCvDtos) {
                        for (Section section : dateSections) {
                            if (section.getTypeName() == sectionCvDto.getTypeName() && section.getTypeId() == sectionCvDto.getTypeId()
                                    && (sectionCvDto.getDuration() == null && sectionCvDto.getTitle() != null)) {
                                atLeastOneMatchdate = true;
                            }
                        }
                    }
                    if (!atLeastOneMatchdate) {
                        Evaluate evaluate1 = evaluateRepository.findById(10);
                        if(evaluate1!=null){
                            scorePractice += evaluate1.getScore();
                        }
                    }
                    break;

                case 10:
                    // Check phone = null
                    Optional<Users> users = usersRepository.findUsersById(userId);
                    boolean atLeastOneMatchPhone = false;
                    if (users.isPresent() && users.get().getPhone() == null) {
                        atLeastOneMatchPhone = true;
                        // Do something
                    }
                    if (!atLeastOneMatchPhone) {
                        Evaluate evaluate1 = evaluateRepository.findById(11);
                        if(evaluate1!=null){
                            scorePractice += evaluate1.getScore();
                        }
                    }
                    break;

                case 11:
                    // Check linkin = null
                    Optional<Users> users1 = usersRepository.findUsersById(userId);
                    boolean atLeastOneMatchLinkin = false;
                    if (users1.isPresent() && users1.get().getLinkin() == null) {
                        // Do something
                        atLeastOneMatchLinkin = true;
                    }
                    if (!atLeastOneMatchLinkin) {
                        Evaluate evaluate1 = evaluateRepository.findById(12);
                        if(evaluate1!=null){
                            scorePractice += evaluate1.getScore();
                        }
                    }
                    break;

                case 12:
                    // Check count word
                    boolean atLeastOneMatchWord = false;
                    if (totalWords[0] < 300) {
                        atLeastOneMatchWord = true;
                    }
                    if (!atLeastOneMatchWord) {
                        Evaluate evaluate1 = evaluateRepository.findById(13);
                        if(evaluate1!=null){
                            scorePractice += evaluate1.getScore();
                        }
                    }
                    break;

                case 13:
                    // Check summary
                    boolean atLeastOneMatchSummary = false;
                    Cv cv1 = cvRepository.findCvById(cvId, BasicStatus.ACTIVE);
                    if (cv1.getSummary() == null || cv1.getSummary().length() < 30) {
                        atLeastOneMatchSummary = true;
                    }
                    if (!atLeastOneMatchSummary) {
                        Evaluate evaluate1 = evaluateRepository.findById(14);
                        if(evaluate1!=null){
                            scorePractice += evaluate1.getScore();
                        }
                    }
                    break;

                default:
                    break;
            }
        }
        return scorePractice;
    }

    private List<ContentDto> evaluateOptimational(List<Evaluate> evaluates, int cvId) {
        List<ContentDto> contentDtoList = new ArrayList<>();

        for (int i = 14; i < 15; i++) {
            Evaluate evaluate = evaluates.get(i);
            List<ContentDetailDto> sameSections = new ArrayList<>();

            switch (i) {
                case 14:
                    // Check Ats = null
                    Cv getCv = cvRepository.findCvById(cvId, BasicStatus.ACTIVE);
                    JobDescription jobDescription = getCv.getJobDescription();
                    if (jobDescription != null) {
                        Integer jobDescriptionId = jobDescription.getId();
                        List<Ats> ats = atsRepository.findAllByJobDescriptionId(jobDescriptionId);
                        if (ats != null) {
                            // Do something
                        }
                    }
                    break;

                default:
                    break;
            }

            contentDtoList.add(new ContentDto(evaluate.getTitle(), evaluate.getDescription(), sameSections));
        }

        return contentDtoList;
    }

    private Double evaluateOptimationalScore(List<Evaluate> evaluates, int cvId) {
        double score = 0.0;

        for (int i = 14; i < 15; i++) {
            Evaluate evaluate = evaluates.get(i);
            List<ContentDetailDto> sameSections = new ArrayList<>();

            switch (i) {
                case 14:
                    // Check Ats = null
                    Cv getCv = cvRepository.findCvById(cvId, BasicStatus.ACTIVE);
                    JobDescription jobDescription = getCv.getJobDescription();
                    if (jobDescription != null) {
                        Integer jobDescriptionId = jobDescription.getId();
                        List<Ats> ats = atsRepository.findAllByJobDescriptionId(jobDescriptionId);
                        if (ats != null) {
                            Evaluate evaluate1 = evaluateRepository.findById(15);
                            score += evaluate1.getScore();
                        }
                    }
                    break;

                default:
                    break;
            }

        }

        return score;
    }

}
