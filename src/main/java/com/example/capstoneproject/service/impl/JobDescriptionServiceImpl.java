package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.AtsDto;
import com.example.capstoneproject.Dto.JobDescriptionDto;
import com.example.capstoneproject.Dto.JobDescriptionViewDto;
import com.example.capstoneproject.Dto.request.JobDescriptionRequest;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.StatusReview;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.mapper.AtsMapper;
import com.example.capstoneproject.mapper.JobDescriptionMapper;
import com.example.capstoneproject.repository.AtsRepository;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.repository.JobDescriptionRepository;
import com.example.capstoneproject.repository.JobPostingRepository;
import com.example.capstoneproject.service.EvaluateService;
import com.example.capstoneproject.service.JobDescriptionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobDescriptionServiceImpl extends AbstractBaseService<JobDescription, JobDescriptionViewDto, Integer> implements JobDescriptionService {

    @Autowired
    JobDescriptionRepository jobDescriptionRepository;

    @Autowired
    JobPostingRepository jobPostingRepository;

    @Autowired
    JobDescriptionMapper jobDescriptionMapper;

    @Autowired
    CvRepository cvRepository;

    @Autowired
    AtsMapper atsMapper;

    @Autowired
    AtsRepository atsRepository;

    @Autowired
    EvaluateService evaluateService;

    @Autowired
    ModelMapper modelMapper;

    public JobDescriptionServiceImpl(JobDescriptionRepository jobDescriptionRepository, JobDescriptionMapper jobDescriptionMapper) {
        super(jobDescriptionRepository, jobDescriptionMapper, jobDescriptionRepository::findById);
        this.jobDescriptionRepository = jobDescriptionRepository;
        this.jobDescriptionMapper = jobDescriptionMapper;
    }

    @Override
    public JobDescriptionViewDto createJobDescription(Integer cvId, JobDescriptionRequest dto, Principal principal) throws JsonProcessingException {
        Optional<Cv> cvOptional = cvRepository.findById(cvId);
        if(cvOptional.isPresent()){
            if(countWords(dto.getDescription())>30 && countWords(dto.getDescription())<1000){
                JobDescription jobDescription = modelMapper.map(dto, JobDescription.class);
                jobDescription.setTitle(dto.getTitle());
                jobDescription.setDescription(dto.getDescription());
                Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByIdAndStatusAndShare(dto.getJobPostingId(), BasicStatus.ACTIVE, StatusReview.Published);
                if(jobPostingOptional.isPresent()){
                    JobPosting jobPosting = jobPostingOptional.get();
                    jobDescription.setJobPosting(jobPosting);
                }
                JobDescription saved = jobDescriptionRepository.save(jobDescription);
                Integer jobId = saved.getId();
                List<AtsDto> atsList = evaluateService.ListAts(cvId,jobId,modelMapper.map(dto, JobDescriptionDto.class),principal);
                JobDescriptionViewDto jobDescriptionViewDto = jobDescriptionMapper.mapEntityToDto(jobDescription);
                jobDescriptionViewDto.setAts(atsList);
                Cv cv = cvOptional.get();
                Optional<JobDescription> jobDescriptionOptional = jobDescriptionRepository.findById(jobId);
                if(jobDescriptionOptional.isPresent()){
                    JobDescription jobDescription1 = jobDescriptionOptional.get();
                    cv.setJobDescription(jobDescription1);
                }
                cv.setCompanyName(dto.getCompany());
                cvRepository.save(cv);
                return jobDescriptionViewDto;
            }else{
                throw new BadRequestException("Description please contain 30 characters or more.");
            }
        }else{
            throw new BadRequestException("Cv ID not found.");
        }
    }

    @Override
    public JobDescriptionViewDto getJobDescription(Integer cvId) throws JsonProcessingException {
        Optional<Cv> cvOptional = cvRepository.findById(cvId);
        if(cvOptional.isPresent()){
            Cv cv = cvOptional.get();
            Integer c = cv.getJobDescription().getId();
            Optional<JobDescription> jobDescription = jobDescriptionRepository.findById(cv.getJobDescription().getId());
            JobDescriptionViewDto jobDescriptionViewDto = new JobDescriptionViewDto();
            if(jobDescription.isPresent()){
                jobDescriptionViewDto.setTitle(jobDescription.get().getTitle());
                jobDescriptionViewDto.setDescription(jobDescription.get().getDescription());
                List<AtsDto> atsList = evaluateService.getAts(cvId,jobDescription.get().getId());
                jobDescriptionViewDto.setAts(atsList);
            }
            return jobDescriptionViewDto;
        }else {
            throw new BadRequestException("CV Id not found");
        }
    }

    @Override
    public JobDescriptionViewDto updateJobDescription(Integer cvId, JobDescriptionRequest dto, Principal principal) throws Exception {
        Optional<Cv> cvOptional = cvRepository.findById(cvId);
        if(cvOptional.isPresent()){
            Cv cv = cvOptional.get();
            cv.setCompanyName(dto.getCompany());
            cvRepository.save(cv);
            if(countWords(dto.getDescription())>30 && countWords(dto.getDescription())<1000){
                Optional<JobDescription> jobDescriptionOptional = jobDescriptionRepository.findById(cv.getJobDescription().getId());
                List<AtsDto> atsList;
                JobDescriptionViewDto jobDescriptionViewDto = new JobDescriptionViewDto();
                if (jobDescriptionOptional.isPresent()) {
                    JobDescription jobDescription = jobDescriptionOptional.get();

                    if(dto.getTitle()==null && jobDescription.getTitle().equals(dto.getTitle())){
                        jobDescription.setTitle(jobDescription.getTitle());
                    }else{
                        jobDescription.setTitle(dto.getTitle());
                    }
                    if(dto.getDescription()!= null){
                        if(isSubstringInString(jobDescription.getDescription(),dto.getDescription()) && jobDescription.getDescription().length()==dto.getDescription().length()){
                            jobDescription.setDescription(jobDescription.getDescription());
                            atsList = evaluateService.getAts(cvId,jobDescription.getId());
                            if(atsList==null){
                                atsList = evaluateService.ListAts(cvId,jobDescription.getId(),modelMapper.map(dto,JobDescriptionDto.class),principal);
                            }
                        }else{
                            jobDescription.setDescription(dto.getDescription());
                            atsRepository.deleteByJobDescriptionId(jobDescription.getId());
                            atsList = evaluateService.ListAts(cvId,jobDescription.getId(),modelMapper.map(dto,JobDescriptionDto.class),principal);
                        }
                    }else{
                        atsList = evaluateService.getAts(cvId,jobDescription.getId());
                    }
                    if(dto.getJobPostingId()!=null){
                        Optional<JobPosting> jobPostingOptional = jobPostingRepository.findByIdAndStatusAndShare(dto.getJobPostingId(), BasicStatus.ACTIVE, StatusReview.Published);
                        if(jobPostingOptional.isPresent()){
                            JobPosting jobPosting = jobPostingOptional.get();
                            jobDescription.setJobPosting(jobPosting);
                        }
                    }
                    JobDescription updatedJobDescription = jobDescriptionRepository.save(jobDescription);
                    jobDescriptionViewDto.setTitle(updatedJobDescription.getTitle());
                    jobDescriptionViewDto.setDescription(updatedJobDescription.getDescription());
                    jobDescriptionViewDto.setAts(atsList);

                    return jobDescriptionViewDto;
                } else {
                    throw new Exception("Job description with ID " + cv.getJobDescription().getId() + " not found.");
                }
            }else {
                throw new BadRequestException("Description must be over 30 words.");
            }
        }else{
            throw new BadRequestException("Cv ID not found");
        }
    }

    public static boolean isSubstringInString(String fullString, String substring) {
        if (fullString == null || substring == null) {
            return false;
        }
        int fullLength = fullString.length();
        int subLength = substring.length();

        int[][] dp = new int[fullLength + 1][subLength + 1];

        for (int i = 1; i <= fullLength; i++) {
            for (int j = 1; j <= subLength; j++) {
                if (fullString.charAt(i - 1) == substring.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp[fullLength][subLength] == subLength;
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
