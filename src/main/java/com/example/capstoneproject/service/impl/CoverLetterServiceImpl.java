package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.CoverLetterViewDto;
import com.example.capstoneproject.entity.CoverLetter;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
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

    @Override
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
                transactionService.chargePerRequest(securityUtil.getLoginUser(principal).getId(), "Generate Cover Letter");
                String response = chatGPTService.chatWithGPTCoverLetter(messagesJson,dto.getTemperature());
                coverLetter.setId(coverLetter.getId());
                coverLetter.setDescription(processString(response));
                coverLetter.setDate(dto.getDate());
                coverLetter.setCompany(dto.getCompany());
                coverLetter.setDear(dto.getDear());
                coverLetter.setJobTitle(dto.getJob_title());
                coverLetter.setJobDescription(dto.getJob_description());
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

    @Override
    public CoverLetterViewDto createCoverLetter(Integer userId, Integer cvId, CoverLetterAddDto dto) {
        Optional<Cv> cvOptional = cvRepository.findByUser_IdAndId(userId,cvId);
        if(cvOptional.isPresent()){
            Cv cv = cvOptional.get();
            List<CoverLetter> coverLetters = coverLetterRepository.findByCv_User_Id(userId);
            if(coverLetters!=null){
                for(CoverLetter coverLetter:coverLetters){
                    if (coverLetter.getTitle().equals(dto.getTitle())) {
                        throw new BadRequestException("Title already exists in another cover letter.");
                    }
                }
            }
            CoverLetter coverLetter = modelMapper.map(dto, CoverLetter.class);
            coverLetter.setTitle(dto.getTitle());
            coverLetter.setCompany(cv.getCompanyName());
            if(cv.getJobDescription()!=null){
                coverLetter.setJobTitle(cv.getJobDescription().getTitle());
                coverLetter.setJobDescription(cv.getJobDescription().getDescription());
            }
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

            if (dto.getJobTitle() != null && !dto.getJobTitle().equals(existingCoverLetter.getJobTitle())) {
                existingCoverLetter.setJobTitle(dto.getJobTitle());
            }

            if (dto.getJobDescription() != null && !dto.getJobDescription().equals(existingCoverLetter.getJobDescription())) {
                existingCoverLetter.setJobDescription(dto.getJobDescription());
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
                // Loại bỏ khoảng trắng ở đầu và cuối chuỗi
                String trimmedDescription = dto.getDescription().trim();

                // Kiểm tra xem sau khi loại bỏ khoảng trắng, chuỗi có trở thành rỗng không
                if (!trimmedDescription.isEmpty()) {
                    // Kiểm tra xem có sự thay đổi so với existingCoverLetter hay không
                    if (existingCoverLetter.getDescription() == null || !trimmedDescription.equals(existingCoverLetter.getDescription())) {
                        // Cập nhật mô tả nếu có sự thay đổi
                        existingCoverLetter.setDescription(trimmedDescription);
                    }
                } else {
                    // Nếu chuỗi sau khi loại bỏ khoảng trắng trở thành rỗng, lấy lại giá trị cũ
                    existingCoverLetter.setDescription(existingCoverLetter.getDescription());
                }
            }

            if(dto.getCvId() !=null){
                Optional<Cv> cvOptional = cvRepository.findByIdAndStatus(dto.getCvId(), BasicStatus.ACTIVE);
                if(cvOptional.isPresent()){
                    Cv cv = cvOptional.get();
                    existingCoverLetter.setCv(cv);
                }else{
                    throw new BadRequestException("This cv id not exist.");
                }
            }

            coverLetterRepository.save(existingCoverLetter);
            return true;
        } else {
            throw new IllegalArgumentException("Cover Letter ID not found");
        }
    }

    @Override
    public boolean updateTitleCoverLetter(Integer coverLetterId, CoverLetterUpdateTitleDto dto) {
        Optional<CoverLetter> existingCoverLetterOptional = coverLetterRepository.findById(coverLetterId);
        if (existingCoverLetterOptional.isPresent()) {
            CoverLetter existingCoverLetter = existingCoverLetterOptional.get();
            List<CoverLetter> coverLetters = coverLetterRepository.findByCv_User_Id(existingCoverLetter.getCv().getUser().getId());
            if (coverLetters != null) {
                for (CoverLetter coverLetter : coverLetters) {
                    if (coverLetter.getId().equals(coverLetterId)) {
                        // Skip the current cover letter being updated
                        continue;
                    }

                    if (coverLetter.getTitle().equals(dto.getTitle())) {
                        throw new BadRequestException("Title already exists in another cover letter.");
                    }
                }
            }

            // Check if the title has changed or not
            if (dto.getTitle() != null && !dto.getTitle().equals(existingCoverLetter.getTitle())) {
                existingCoverLetter.setTitle(dto.getTitle());
                coverLetterRepository.save(existingCoverLetter);
                return true;
            } else {
                // Title hasn't changed, no need to save
                return true;
            }
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
                coverLetterDto.setJobTitle(coverLetter.getJobTitle());
                coverLetterDto.setJobDescription(coverLetter.getJobDescription());
                coverLetterDto.setCvId(coverLetter.getCv().getId());
                coverLetterDto.setResumeName(coverLetter.getCv().getResumeName());
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
//        transactionService.chargePerRequest(securityUtil.getLoginUser(principal).getId(), );
        String response = chatGPTService.chatWithGPTCoverLetterRevise(messagesJson);
        chatResponse.setReply(response);
        return chatResponse;
    }

    @Override
    public String duplicateCoverLetter(Integer userId, Integer coverLetterId) {
        boolean isCoverLetter = coverLetterRepository.existsByCv_User_IdAndId(userId, coverLetterId);

        if (isCoverLetter) {
            Optional<CoverLetter> optionalCoverLetter = coverLetterRepository.findById(coverLetterId);

            if (optionalCoverLetter.isPresent()) {
                CoverLetter originalCoverLetter = optionalCoverLetter.get();
                List<CoverLetter> coverLetters = coverLetterRepository.findByCv_User_Id(userId);

                CoverLetter duplicatedCoverLetter = new CoverLetter();
                duplicatedCoverLetter.setTitle("Copy of "+originalCoverLetter.getTitle()+coverLetters.size());
                duplicatedCoverLetter.setCompany(originalCoverLetter.getCompany());
                duplicatedCoverLetter.setDate(originalCoverLetter.getDate());
                duplicatedCoverLetter.setDear(originalCoverLetter.getDear());
                duplicatedCoverLetter.setDescription(originalCoverLetter.getDescription());
                duplicatedCoverLetter.setCv(originalCoverLetter.getCv());
                duplicatedCoverLetter.setJobTitle(originalCoverLetter.getJobTitle());
                duplicatedCoverLetter.setJobDescription(originalCoverLetter.getJobDescription());

                CoverLetter savedCoverLetter = coverLetterRepository.save(duplicatedCoverLetter);

                return "Cover Letter duplicated successfully";
            } else {
                throw new RuntimeException("Original Cover Letter not found.");
            }
        } else {
            throw new IllegalArgumentException("Cover Letter with ID " + coverLetterId + " does not belong to User with ID " + userId);
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
