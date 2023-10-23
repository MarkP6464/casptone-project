package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.CoverLetterViewDto;
import com.example.capstoneproject.entity.CoverLetter;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.Users;
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
import org.springframework.stereotype.Service;

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
                "You will write the cover letter in a modern, professional style without being too formal, as a modern employee might do naturally.\n" +
                "Please generate a cover letter without 'Dear' and 'Sincerely'.";
//        String coverLetterWithAWittyRemark = "You are a cover letter generator.\n" +
//                "You will be given a job description along with the job applicant's resume.\n" +
//                "You will write a cover letter for the applicant that matches their past experiences from the resume with the job description. Write the cover letter in the same language as the job description provided!\n" +
//                "Rather than simply outlining the applicant's past experiences, you will give more detail and explain how those experiences will help the applicant succeed in the new job.\n" +
//                "You will write the cover letter in a modern, relaxed style, as a modern employee might do naturally.\n" +
//                "Include a job related joke at the end of the cover letter.";
//        String ideasForCoverLetter = "You are a cover letter idea generator. You will be given a job description along with the job applicant's resume. You will generate a bullet point list of ideas for the applicant to use in their cover letter. ";
//        String command ;
//
//        if (isCompleteCoverLetter) {
//            command = includeWittyRemark ? coverLetterWithAWittyRemark : completeCoverLetter;
//        } else {
//            command = ideasForCoverLetter;
//        }
        String content = "";
        String userMessage = "";
        Optional<Cv> cvsOptional = cvRepository.findById(cvId);
        if(cvsOptional.isPresent()){
            Cv cv = cvsOptional.get();
            content = cv.getCvBody();

        }
        userMessage = "My Resume: " + content + ". Job title: " + job_title + "Company: " + company +  " Job Description: " + job_description + ".";
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
        chatResponse.setReply(response);
        return chatResponse;
    }
    public ChatResponse generateEvaluate(float temperature, String description) throws JsonProcessingException {
        String completeCoverLetter = "You are the person reviewing your CV\n" +
                "You will be provided with a job description along with relevant applicant information fields.\n" +
                "You'll check for trendy terms or phrases in resumes that may make job seekers sound generic and unoriginal, not effectively highlighting their unique skills and qualifications. fruit.\n" +
                "Please provide an answer in the following format: Try replacing \"responsible for\", \"responsible\", \"in charge of\" in this section.";
        String userMessage = "My Description: " + description + ".";
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
        chatResponse.setReply(splitStringOnFirstNewLine(response));
        return chatResponse;
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

    public String splitStringOnFirstNewLine(String input) {
        String[] lines = input.split("\n");
        if (lines.length > 0) {
            return lines[0];
        }
        return input;
    }

}
