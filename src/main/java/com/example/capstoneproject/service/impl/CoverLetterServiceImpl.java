package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.CoverLetterViewDto;
import com.example.capstoneproject.entity.CoverLetter;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.Experience;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.SectionEvaluate;
import com.example.capstoneproject.mapper.CoverLetterMapper;
import com.example.capstoneproject.repository.CoverLetterRepository;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.repository.UsersRepository;
import com.example.capstoneproject.service.CoverLetterService;
import com.example.capstoneproject.service.UsersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;

@Service
public class CoverLetterServiceImpl extends AbstractBaseService<CoverLetter, CoverLetterDto, Integer> implements CoverLetterService {

    @Autowired
    ChatGPTServiceImpl chatGPTService;

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

    public ChatResponse generateCoverLetter(float temperature,String job_title, int cvId, String company, String job_description) throws JsonProcessingException {
        String completeCoverLetter = "You are a cover letter generator.\n" +
                "You will be given a job description along with the job applicant's resume.\n" +
                "You will write a cover letter for the applicant that matches their past experiences from the resume with the job description. Write the cover letter in the same language as the job description provided!\n" +
                "Rather than simply outlining the applicant's past experiences, you will give more detail and explain how those experiences will help the applicant succeed in the new job.\n" +
                "You will write the cover letter in a modern, professional style without being too formal, as a modern employee might do naturally.";
        String content = "";
        String userMessage = "";
        Optional<Cv> cvsOptional = cvRepository.findById(cvId);
        if(cvsOptional.isPresent()){
            Cv cv = cvsOptional.get();
            content = cv.getCvBody();

        }
        userMessage = "My Resume: " + content + ". Job title: " + job_title + " Company: " + company +  " Job Description: " + job_description + ".";
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
        String response = chatGPTService.chatWithGPTCoverLetter(messagesJson,temperature);
        ChatResponse chatResponse = new ChatResponse();
        chatResponse.setReply(processString(response));
        return chatResponse;
    }

    public ChatResponse generateSummaryCV(float temperature, Integer cvId, String position_highlight, String skill_highlight) throws JsonProcessingException {
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
            if(position_highlight!=null && skill_highlight!=null){
                completeSystem = "You are an expert in CV writing and your task is to create a resume personal statement that will be placed at the beginning of the CV. \n" +
                        "The personal statement should effectively introduce the candidate to the hiring manager and highlight why they would be a fantastic hire.\n" +
                        "The personal statement should be concise, consisting of 2-3 sentences and spanning between 30-50 words. \n" +
                        "It should begin with an attention-grabbing opening hook and clearly state the desired position as a " + position_highlight + ". \n" +
                        "Soft skills and hard skills, " + skill_highlight + ", should be highlighted. \n" +
                        "Impressive facts and statistics should be incorporated, and the candidate’s short and long-term goals should be briefly mentioned.";
            }else if(position_highlight==null && skill_highlight==null){
                completeSystem = "You are an expert in CV writing and your task is to create a resume personal statement that will be placed at the beginning of the CV. \n" +
                        "The personal statement should effectively introduce the candidate to the hiring manager and highlight why they would be a fantastic hire.\n" +
                        "The personal statement should be concise, consisting of 2-3 sentences and spanning between 30-50 words. \n" +
                        "It should begin with an attention-grabbing opening hook and clearly state the desired position base on the past experience. \n" +
                        "Soft skills and hard skills should be highlighted. Impressive facts and statistics should be incorporated, and the candidate’s short and long-term goals should be briefly mentioned.";
            }else if(position_highlight == null){
                completeSystem = "You are an expert in CV writing and your task is to create a resume personal statement that will be placed at the beginning of the CV. \n" +
                        "The personal statement should effectively introduce the candidate to the hiring manager and highlight why they would be a fantastic hire.\n" +
                        "The personal statement should be concise, consisting of 2-3 sentences and spanning between 30-50 words. \n" +
                        "It should begin with an attention-grabbing opening hook and clearly state the desired position base on the past experience. \n" +
                        "Soft skills and hard skills, " + skill_highlight + ", should be highlighted. \n" +
                        "Impressive facts and statistics should be incorporated, and the candidate’s short and long-term goals should be briefly mentioned.";
            }else {
                completeSystem = "You are an expert in CV writing and your task is to create a resume personal statement that will be placed at the beginning of the CV. \n" +
                        "The personal statement should effectively introduce the candidate to the hiring manager and highlight why they would be a fantastic hire.\n" +
                        "The personal statement should be concise, consisting of 2-3 sentences and spanning between 30-50 words. \n" +
                        "It should begin with an attention-grabbing opening hook and clearly state the desired position as a " + position_highlight + ". \n" +
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
            String response = chatGPTService.chatWithGPTCoverLetter(messagesJson,temperature);
            ChatResponse chatResponse = new ChatResponse();
            chatResponse.setReply(response);
            return chatResponse;
        }else{
            throw new RuntimeException("Please add experience into CV");
        }
    }

    @Override
    public CoverLetterViewDto createCoverLetter(Integer userId, CoverLetterAddDto dto) {
        CoverLetter coverLetter = modelMapper.map(dto, CoverLetter.class);
        Users Users = usersService.getUsersById(userId);
        coverLetter.setTitle(dto.getTitle());
        coverLetter.setUser(Users);
        CoverLetter saved = coverLetterRepository.save(coverLetter);
        CoverLetterViewDto coverLetterViewDto = new CoverLetterViewDto();
        coverLetterViewDto.setId(saved.getId());
        coverLetterViewDto.setTitle(saved.getTitle());
        return modelMapper.map(saved, CoverLetterViewDto.class);
    }

    @Override
    public boolean updateCoverLetter(int UsersId, int coverLetterId, CoverLetterUpdateDto dto) {
        Optional<CoverLetter> existingCoverLetterOptional = coverLetterRepository.findById(coverLetterId);
        if (existingCoverLetterOptional.isPresent()) {
            CoverLetter existingCoverLetter = existingCoverLetterOptional.get();
            if (existingCoverLetter.getUser().getId() != UsersId) {
                throw new IllegalArgumentException("Cover Letter does not belong to Users with id " + UsersId);
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
        boolean isCoverLetter = coverLetterRepository.existsByUser_IdAndId(UsersId, coverLetterId);

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
        boolean isCoverLetter = coverLetterRepository.existsByUser_IdAndId(userId, coverLetterId);
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
                coverLetterDto.setUser(modelMapper.map(users, UserCoverLetterDto.class));
            }
        }else {
            throw new IllegalArgumentException("cover letter with ID " + coverLetterId + " does not belong to Users with ID " + userId);
        }
        return coverLetterDto;
    }

    @Override
    public ChatResponse reviseCoverLetter(String content, String improvement) throws JsonProcessingException {
        String revise = "You are a cover letter editor. You will be given a piece of isolated text from within a cover letter and told how you can improve it. Only respond with the revision. Make sure the revision is in the same language as the given isolated text.";
        String userMessage = "Isolated text from within cover letter: " + content + ". It should be improved by making it more: " + improvement;
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
        String response = chatGPTService.chatWithGPTCoverLetterRevise(messagesJson);
        chatResponse.setReply(response);
        return chatResponse;
    }

    public String processString(String input) {
        int index = input.indexOf("\n\n");
        if (index != -1) {
            return input.substring(index + 2);
        }
        return input;
    }
}
