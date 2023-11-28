package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.CoverLetterViewDto;
import com.example.capstoneproject.entity.CoverLetter;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.mapper.CoverLetterMapper;
import com.example.capstoneproject.repository.CoverLetterRepository;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.repository.UsersRepository;
import com.example.capstoneproject.service.CoverLetterService;
import com.example.capstoneproject.service.HistorySummaryService;
import com.example.capstoneproject.service.TransactionService;
import com.example.capstoneproject.service.UsersService;
import com.example.capstoneproject.utils.SecurityUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CoverLetterServiceImpl extends AbstractBaseService<CoverLetter, CoverLetterDto, Integer> implements CoverLetterService {

    @Autowired
    ChatGPTServiceImpl chatGPTService;

    @Autowired
    SecurityUtil securityUtil;

    @Autowired
    TransactionService transactionService;

    @Autowired
    HistorySummaryService historySummaryService;

    @Autowired
    CoverLetterRepository coverLetterRepository;

    @Autowired
    CoverLetterMapper coverLetterMapper;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UsersService usersService;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    CvRepository cvRepository;

    public CoverLetterServiceImpl(CoverLetterRepository coverLetterRepository, CoverLetterMapper coverLetterMapper) {
        super(coverLetterRepository, coverLetterMapper, coverLetterRepository::findById);
        this.coverLetterRepository = coverLetterRepository;
        this.coverLetterMapper = coverLetterMapper;
    }

    public ChatResponse generateCoverLetter(Integer coverId,  Integer cvId, CoverLetterGenerationDto dto, Principal principal) throws JsonProcessingException {
        Optional<Cv> cvOptional = cvRepository.findById(cvId);
        if(cvOptional.isPresent()){
            Cv cv = cvOptional.get();
            Optional<CoverLetter> coverLetterOptional = coverLetterRepository.findById(coverId);
            if(coverLetterOptional.isPresent()){
                CoverLetter coverLetter = coverLetterOptional.get();
                String completeCoverLetter = "You are a cover letter generator.\n" +
                        "You will be given a job description along with the job applicant's resume.\n" +
                        "You will write a cover letter for the applicant that matches their past experiences from the resume with the job description. Write the cover letter in the same language as the job description provided!\n" +
                        "Rather than simply outlining the applicant's past experiences, you will give more detail and explain how those experiences will help the applicant succeed in the new job.\n" +
                        "You will write the cover letter in a modern, professional style without being too formal, as a modern employee might do naturally.";
                String content = cv.getCvBody();
                String userMessage = "";
//                Optional<Cv> cvsOptional = cvRepository.findById(cvId);
//                if(cvsOptional.isPresent()){
//                    Cv cv = cvsOptional.get();
//                    content = cv.getCvBody();
//
//                }
                userMessage = "My Resume: " + content + ". Job title: " + dto.getJob_title() + " Company: " + dto.getCompany() +  " Job Description: " + dto.getJob_description() + ".";
                List<Map<String, Object>> messagesList = new ArrayList<>();
                Map<String, Object> systemMessage = new HashMap<>();
                systemMessage.put("role", "system");
                systemMessage.put("content", completeCoverLetter);
                messagesList.add(systemMessage);
                Map<String, Object> userMessageMap = new HashMap<>();
                userMessageMap.put("role", "user");
                userMessageMap.put("content", userMessage);
                messagesList.add(userMessageMap);
                String messagesJson = new ObjectMapper().writeValueAsString(messagesList);
                transactionService.chargePerRequest(securityUtil.getLoginUser(principal).getId());
                String response = chatGPTService.chatWithGPTCoverLetter(messagesJson,dto.getTemperature());
                coverLetter.setId(coverLetter.getId());
                coverLetter.setDescription(processString(response));
                coverLetter.setDate(dto.getDate());
                coverLetter.setCompany(dto.getCompany());
                coverLetter.setDear(dto.getDear());
                coverLetter.setCv(cv);
                coverLetterRepository.save(coverLetter);
                ChatResponse chatResponse = new ChatResponse();
                chatResponse.setReply(processString(response));
                return chatResponse;
            }else{
                throw new BadRequestException("Cover Letter ID not found.");
            }
        }else{
            throw new BadRequestException("CV ID not found");
        }

    }

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
            transactionService.chargePerRequest(securityUtil.getLoginUser(principal).getId());
            String response = chatGPTService.chatWithGPTCoverLetter(messagesJson,1);
            ChatResponse chatResponse = new ChatResponse();
            chatResponse.setReply(response);
            historySummaryService.createHistorySummary(cv.getId(), response);
            return chatResponse;
        }else{
            throw new BadRequestException("Please add experience into CV");
        }
    }

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
                    .sorted(Comparator.comparingInt(ExperienceDto::getTheOrder))
                    .collect(Collectors.toList());

            displayItems.addAll(displayExperiences);

            List<ProjectDto> displayProjects = cvBodyDto.getProjects().stream()
                    .filter(ProjectDto::getIsDisplay)
                    .sorted(Comparator.comparingInt(ProjectDto::getTheOrder))
                    .collect(Collectors.toList());

            displayItems.addAll(displayProjects);

            List<InvolvementDto> displayInvolvements = cvBodyDto.getInvolvements().stream()
                    .filter(InvolvementDto::getIsDisplay)
                    .sorted(Comparator.comparingInt(InvolvementDto::getTheOrder))
                    .collect(Collectors.toList());

            displayItems.addAll(displayInvolvements);

            List<EducationDto> displayEducations = cvBodyDto.getEducations().stream()
                    .filter(EducationDto::getIsDisplay)
                    .sorted(Comparator.comparingInt(EducationDto::getTheOrder))
                    .collect(Collectors.toList());

            displayItems.addAll(displayEducations);

            List<CertificationDto> displayCertifications = cvBodyDto.getCertifications().stream()
                    .filter(CertificationDto::getIsDisplay)
                    .sorted(Comparator.comparingInt(CertificationDto::getTheOrder))
                    .collect(Collectors.toList());

            displayItems.addAll(displayCertifications);

            List<SkillDto> displaySkills = cvBodyDto.getSkills().stream()
                    .filter(SkillDto::getIsDisplay)
                    .sorted(Comparator.comparingInt(SkillDto::getTheOrder))
                    .collect(Collectors.toList());

            displayItems.addAll(displaySkills);

            // Sắp xếp danh sách chung displayItems theo theOrder1 từ thấp đến cao
            displayItems.sort(Comparator.comparingInt(item -> {
                if (item instanceof ExperienceDto) {
                    return ((ExperienceDto) item).getTheOrder();
                } else if (item instanceof ProjectDto) {
                    return ((ProjectDto) item).getTheOrder();
                } else if (item instanceof InvolvementDto) {
                    return ((InvolvementDto) item).getTheOrder();
                } else if (item instanceof EducationDto) {
                    return ((EducationDto) item).getTheOrder();
                } else if (item instanceof CertificationDto) {
                    return ((CertificationDto) item).getTheOrder();
                } else if (item instanceof SkillDto) {
                    return ((SkillDto) item).getTheOrder();
                }
                return 0;
            }));

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
            transactionService.chargePerRequest(securityUtil.getLoginUser(principal).getId());
            String response = chatGPTService.chatWithGPTCoverLetter(messagesJson,temperature);
            ChatResponse chatResponse = new ChatResponse();
            chatResponse.setReply(response);
            return chatResponse;
        }else{
            throw new BadRequestException("Please add experience into CV");
        }
    }

    @Override
    public CoverLetterViewDto createCoverLetter(Integer userId, Integer cvId, CoverLetterAddDto dto) {
        Optional<Cv> cvOptional = cvRepository.findByUser_IdAndId(userId,cvId);
        if(cvOptional.isPresent()){
            Cv cv = cvOptional.get();
            CoverLetter coverLetter = modelMapper.map(dto, CoverLetter.class);
            coverLetter.setTitle(dto.getTitle());
            coverLetter.setCv(cv);
            CoverLetter saved = coverLetterRepository.save(coverLetter);
            CoverLetterViewDto coverLetterViewDto = new CoverLetterViewDto();
            coverLetterViewDto.setId(saved.getId());
            coverLetterViewDto.setTitle(saved.getTitle());
            return modelMapper.map(saved, CoverLetterViewDto.class);
        }else{
            throw new BadRequestException("Cv ID not found.");
        }
    }

    @Override
    public List<CoverLetterViewDto> getAllCoverLetter(Integer userId) {
        List<CoverLetter> coverLetters = coverLetterRepository.findByCv_User_Id(userId);
        List<CoverLetterViewDto> coverLetterViewDtos = new ArrayList<>();
        if (coverLetters != null && !coverLetters.isEmpty()) {
            for (CoverLetter coverLetter : coverLetters) {
                CoverLetterViewDto coverLetterViewDto = new CoverLetterViewDto();
                coverLetterViewDto.setId(coverLetter.getId());
                coverLetterViewDto.setTitle(coverLetter.getTitle());
                coverLetterViewDtos.add(coverLetterViewDto);
            }
        } else {
            throw new BadRequestException("Currently, the system does not find any Cover Letter that exists in User.");
        }
        return coverLetterViewDtos;
    }

    @Override
    public boolean updateCoverLetter(Integer cvId, Integer coverLetterId, CoverLetterUpdateDto dto) {
        Optional<CoverLetter> existingCoverLetterOptional = coverLetterRepository.findById(coverLetterId);
        if (existingCoverLetterOptional.isPresent()) {
            CoverLetter existingCoverLetter = existingCoverLetterOptional.get();
            if (!Objects.equals(existingCoverLetter.getCv().getId(), cvId)) {
                throw new IllegalArgumentException("Cover Letter does not belong to CV with id " + cvId);
            }
            if (dto.getTitle() != null && !dto.getTitle().equals(existingCoverLetter.getTitle())) {
                existingCoverLetter.setTitle(dto.getTitle());
            }

            if (dto.getDear() != null && !dto.getDear().equals(existingCoverLetter.getDear())) {
                existingCoverLetter.setDear(dto.getDear());
            }

            if (dto.getDate() != null) {
                if (existingCoverLetter.getDate() == null || !dto.getDate().equals(existingCoverLetter.getDate())) {
                    existingCoverLetter.setDate(dto.getDate());
                }
            }

            if (dto.getCompany() != null) {
                if (existingCoverLetter.getCompany() == null || !dto.getCompany().equals(existingCoverLetter.getCompany())) {
                    existingCoverLetter.setCompany(dto.getCompany());
                }
            }

            if (dto.getDescription() != null) {
                if (existingCoverLetter.getDescription() == null || !dto.getDescription().equals(existingCoverLetter.getDescription())) {
                    existingCoverLetter.setDescription(dto.getDescription());
                }
            }
            CoverLetter updated = coverLetterRepository.save(existingCoverLetter);
            return true;
        } else {
            throw new IllegalArgumentException("Cover Letter ID not found");
        }
    }

    @Override
    public boolean deleteCoverLetterById(Integer UsersId, Integer coverLetterId) {
        boolean isCoverLetter = coverLetterRepository.existsByCv_User_IdAndId(UsersId, coverLetterId);

        if (isCoverLetter) {
            Optional<CoverLetter> Optional = coverLetterRepository.findById(coverLetterId);
            if (Optional.isPresent()) {
                CoverLetter coverLetter = Optional.get();
                coverLetterRepository.delete(coverLetter);
                return true;
            }else {
                return false;
            }
        } else {
            throw new IllegalArgumentException("cover letter with ID " + coverLetterId + " does not belong to Users with ID " + UsersId);
        }
    }

    @Override
    public CoverLetterDto getCoverLetter(Integer userId, Integer coverLetterId) {
        boolean isCoverLetter = coverLetterRepository.existsByCv_User_IdAndId(userId, coverLetterId);
        CoverLetterDto coverLetterDto = new CoverLetterDto();
        if (isCoverLetter){
            Optional<CoverLetter> coverLetterOptional = coverLetterRepository.findById(coverLetterId);
            Optional<Users> usersOptional = usersRepository.findUsersById(userId);
            if (coverLetterOptional.isPresent() && usersOptional.isPresent()){
                CoverLetter coverLetter = coverLetterOptional.get();
                Users users = usersOptional.get();
                coverLetterDto.setId(coverLetter.getId());
                coverLetterDto.setTitle(coverLetter.getTitle());
                coverLetterDto.setDear(coverLetter.getDear());
                coverLetterDto.setDate(coverLetter.getDate());
                coverLetterDto.setCompany(coverLetter.getCompany());
                coverLetterDto.setDescription(coverLetter.getDescription());
                coverLetterDto.setCvId(coverLetter.getCv().getId());
                coverLetterDto.setUser(modelMapper.map(users, UserCoverLetterDto.class));
            }
        }else {
            throw new IllegalArgumentException("cover letter with ID " + coverLetterId + " does not belong to Users with ID " + userId);
        }
        return coverLetterDto;
    }

    @Override
    public ChatResponse reviseCoverLetter(CoverLetterReviseDto dto, Principal principal) throws JsonProcessingException {
        String revise = "You are a cover letter editor. You will be given a piece of isolated text from within a cover letter and told how you can improve it. Only respond with the revision. Make sure the revision is in the same language as the given isolated text.";
        String userMessage = "Isolated text from within cover letter: " + dto.getContent() + ". It should be improved by making it more: " + dto.getImprovement();
        ChatResponse chatResponse = new ChatResponse();
        List<Map<String, Object>> messagesList = new ArrayList<>();
        Map<String, Object> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", revise);
        messagesList.add(systemMessage);
        Map<String, Object> userMessageMap = new HashMap<>();
        userMessageMap.put("role", "user");
        userMessageMap.put("content", userMessage);
        messagesList.add(userMessageMap);
        String messagesJson = new ObjectMapper().writeValueAsString(messagesList);
        transactionService.chargePerRequest(securityUtil.getLoginUser(principal).getId());
        String response = chatGPTService.chatWithGPTCoverLetterRevise(messagesJson);
        chatResponse.setReply(response);
        return chatResponse;
    }

    @Override
    public ChatResponse rewritteExperience(ReWritterExperienceDto dto, Principal principal) throws JsonProcessingException {
        if(dto.getJobTitle()!=null && dto.getBullet()!=null){
            String system = "Improve writing prompt\n" +
                    "As an expert in CV writing, your task is to enhance the description of experience as a " + dto.getJobTitle() + ". The revised writing should keep the original content and adhere to best practices in CV writing, including short, concise bullet points, quantify if possible , focusing on achievements rather than responsibilities. Your response solely provide the content base on the current description provided:";
            String userMessage = "“" + dto.getBullet() + "”\n" +
                    "Start with bullet point don’t need to elaborate any unnecessary information";
            ChatResponse chatResponse = new ChatResponse();
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
            transactionService.chargePerRequest(securityUtil.getLoginUser(principal).getId());
            String response = chatGPTService.chatWithGPTCoverLetterRevise(messagesJson);
            chatResponse.setReply(response);
            return chatResponse;
        }else{
            throw new BadRequestException("Please enter full job title and description.");
        }
    }

    public String processString(String input) {
        int index = input.indexOf("\n\n");
        if (index != -1) {
            return input.substring(index + 2);
        }
        return input;
    }
}
