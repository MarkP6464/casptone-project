package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.exception.ResourceNotFoundException;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.HistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    HistoryCoverLetterRepository historyCoverLetterRepository;

    @Autowired
    JobPostingRepository jobPostingRepository;

    @Autowired
    ApplicationLogRepository applicationLogRepository;

    @Autowired
    CvRepository cvRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PrettyTime prettyTime;

    @Override
    public HistoryViewDto create(Integer userId, Integer cvId) throws JsonProcessingException {
        Optional<Cv> cvOptional = cvRepository.findByUser_IdAndId(userId, cvId);
        History history = new History();
        Instant currentInstant = Instant.now();
        Timestamp timestamp = Timestamp.from(currentInstant);
        if(cvOptional.isPresent()){
            Cv cv = cvOptional.get();
            CvBodyDto cvBodyDto = cv.deserialize();
            CvBodyReviewDto cvBodyReviewDto = new CvBodyReviewDto();
            cvBodyReviewDto.setCvStyle(cvBodyDto.getCvStyle());
            cvBodyReviewDto.setTemplateType(cvBodyDto.getTemplateType());
            cvBodyReviewDto.setTheOrder(cvBodyDto.getTheOrder());
            cvBodyReviewDto.setSkills(cvBodyDto.getSkills());
            cvBodyReviewDto.setCertifications(cvBodyDto.getCertifications());
            cvBodyReviewDto.setExperiences(cvBodyDto.getExperiences());
            cvBodyReviewDto.setEducations(cvBodyDto.getEducations());
            cvBodyReviewDto.setInvolvements(cvBodyDto.getInvolvements());
            cvBodyReviewDto.setProjects(cvBodyDto.getProjects());
            cvBodyReviewDto.setSummary(cv.getSummary());
            cvBodyReviewDto.setName(cvBodyDto.getName());
            cvBodyReviewDto.setAddress(cvBodyDto.getCity());
            cvBodyReviewDto.setPhone(cvBodyDto.getPhone());
            cvBodyReviewDto.setPersonalWebsite(cvBodyDto.getPersonalWebsite());
            cvBodyReviewDto.setEmail(cvBodyDto.getEmail());
            cvBodyReviewDto.setLinkin(cvBodyDto.getLinkin());
            // Sử dụng ObjectMapper để chuyển đổi CvBodyReviewDto thành chuỗi JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String cvBodyReviewJson = objectMapper.writeValueAsString(cvBodyReviewDto);
            history.setCvBody(cvBodyReviewJson);
            history.setTimestamp(timestamp);
            history.setCv(cv);
            return modelMapper.map(historyRepository.save(history), HistoryViewDto.class);
        }else {
            throw new BadRequestException("UserID not exist this CvID");
        }
    }

    @Override
    public List<HistoryDateViewDto> getListHistoryDate(Integer userId, Integer cvId) {
        Optional<Cv> cvOptional = cvRepository.findByUser_IdAndId(userId, cvId);
        if (cvOptional.isPresent()) {
            Cv cv = cvOptional.get();
            List<History> histories = historyRepository.findAllByCv_IdAndCv_StatusOrderByTimestampDesc(cv.getId(), BasicStatus.ACTIVE);
            return histories.stream()
                    .map(history -> {
                        HistoryDateViewDto dto = modelMapper.map(history, HistoryDateViewDto.class);
                        dto.setTimestamp(prettyTime.format(history.getTimestamp()));
                        return dto;
                    })
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    @Override
    public HistoryDto getHistory(Integer historyId) throws JsonProcessingException {
        HistoryDto historyViewDto = new HistoryDto();
        Optional<History> historyOptional = historyRepository.findById(historyId);
        if(historyOptional.isPresent()){
            History history = historyOptional.get();
            CvBodyReviewDto cvBodyReviewDto = history.deserialize();
            historyViewDto.setId(history.getId());
            historyViewDto.setTimestamp(history.getTimestamp());
            historyViewDto.setCvBody(cvBodyReviewDto);
            historyViewDto.setCvId(history.getCv().getId());
            JobPostingApplyResponse jobPostingApplyResponse = new JobPostingApplyResponse();
            Optional<ApplicationLog> applicationLogOptional = applicationLogRepository.findByCv_Id(history.getId());
            if(applicationLogOptional.isPresent()){
                ApplicationLog applicationLog = applicationLogOptional.get();
                Optional<JobPosting> jobPostingOptional = jobPostingRepository.findById(applicationLog.getJobPosting().getId());
                if(jobPostingOptional.isPresent()){
                    JobPosting jobPosting = jobPostingOptional.get();
                    jobPostingApplyResponse.setId(jobPosting.getId());
                    jobPostingApplyResponse.setTitle(jobPosting.getTitle());
                    jobPostingApplyResponse.setWorkingType(jobPosting.getWorkingType());
                    jobPostingApplyResponse.setCompanyName(jobPosting.getCompanyName());
                    jobPostingApplyResponse.setAvatar(jobPosting.getAvatar());
                    jobPostingApplyResponse.setLocation(jobPosting.getLocation());
                    jobPostingApplyResponse.setAbout(jobPosting.getAbout());
                    jobPostingApplyResponse.setBenefit(jobPosting.getBenefit());
                    jobPostingApplyResponse.setDescription(jobPosting.getDescription());
                    jobPostingApplyResponse.setRequirement(jobPosting.getRequirement());
                    jobPostingApplyResponse.setSalary(jobPosting.getSalary());
                    jobPostingApplyResponse.setSkill(jobPosting.getSkill().split(","));
                    jobPostingApplyResponse.setDeadline(jobPosting.getDeadline());
                    jobPostingApplyResponse.setCreateDate(jobPosting.getCreateDate());
                    jobPostingApplyResponse.setUpdateDate(jobPosting.getUpdateDate());
                    jobPostingApplyResponse.setStatus(jobPosting.getStatus());
                    jobPostingApplyResponse.setApply(jobPosting.getApplyAgain());
                    jobPostingApplyResponse.setShare(jobPosting.getShare());

                    List<HistoryCoverLetterResponse> historyCoverLetterResponses = new ArrayList<>();

                    List<ApplicationLog> applicationLogs = applicationLogRepository.findAllByCv_IdAndJobPosting_Id(history.getId(),jobPosting.getId());
                    if(applicationLogs!=null){
                        for(ApplicationLog applicationLog1: applicationLogs){
                            HistoryCoverLetterResponse coverLetterDto = new HistoryCoverLetterResponse();
                            Optional<HistoryCoverLetter> historyCoverLetterOptional = historyCoverLetterRepository.findById(applicationLog1.getCoverLetter().getId());
                            if(historyCoverLetterOptional.isPresent()){
                                HistoryCoverLetter historyCoverLetter = historyCoverLetterOptional.get();
                                coverLetterDto.setId(historyCoverLetter.getId());
                                coverLetterDto.setTitle(historyCoverLetter.getTitle());
                                coverLetterDto.setDear(historyCoverLetter.getDear());
                                coverLetterDto.setDate(historyCoverLetter.getDate());
                                coverLetterDto.setDescription(historyCoverLetter.getDescription());
                                UserCoverLetterDto userCoverLetterDto = new UserCoverLetterDto();
                                userCoverLetterDto.setName(historyCoverLetter.getCoverLetter().getCv().getUser().getName());
                                userCoverLetterDto.setAddress(historyCoverLetter.getCoverLetter().getCv().getUser().getAddress());
                                userCoverLetterDto.setPhone(historyCoverLetter.getCoverLetter().getCv().getUser().getPhone());
                                userCoverLetterDto.setEmail(historyCoverLetter.getCoverLetter().getCv().getUser().getEmail());
                                coverLetterDto.setUser(userCoverLetterDto);
                                CoverLetterViewDto coverLetterViewDto = new CoverLetterViewDto();
                                coverLetterViewDto.setId(historyCoverLetter.getCoverLetter().getId());
                                coverLetterViewDto.setTitle(historyCoverLetter.getCoverLetter().getTitle());
                                coverLetterDto.setCoverLetter(coverLetterViewDto);
                                historyCoverLetterResponses.add(coverLetterDto);
                            }
                        }
                    }
                    jobPostingApplyResponse.setHistoryCoverLetters(historyCoverLetterResponses);
                }
            }
            historyViewDto.setJobPosting(jobPostingApplyResponse);
            return  historyViewDto;
        }else {
            throw new ResourceNotFoundException("History ID not exist into Cv ID");
        }
    }


    @Override
    public HistoryDto getHistoryById(Integer historyId){

        Optional<History> historyOptional = historyRepository.findById(historyId);
        if (historyOptional.isPresent()){
            History history = historyOptional.get();
            HistoryDto historyDto = modelMapper.map(history, HistoryDto.class);
            historyDto.setCvId(history.getCv().getId());
            return historyDto;
        } else throw new ResourceNotFoundException("Not found history of the cover letter id!");
    }

}
