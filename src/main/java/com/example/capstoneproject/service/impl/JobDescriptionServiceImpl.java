package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.AtsDto;
import com.example.capstoneproject.Dto.JobDescriptionDto;
import com.example.capstoneproject.Dto.JobDescriptionViewDto;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.mapper.AtsMapper;
import com.example.capstoneproject.mapper.JobDescriptionMapper;
import com.example.capstoneproject.repository.AtsRepository;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.repository.JobDescriptionRepository;
import com.example.capstoneproject.service.EvaluateService;
import com.example.capstoneproject.service.JobDescriptionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JobDescriptionServiceImpl extends AbstractBaseService<JobDescription, JobDescriptionViewDto, Integer> implements JobDescriptionService {

    @Autowired
    JobDescriptionRepository jobDescriptionRepository;

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
    public JobDescriptionViewDto createJobDescription(Integer cvId, JobDescriptionDto dto) throws JsonProcessingException {
        Optional<Cv> cvOptional = cvRepository.findById(cvId);
        if(cvOptional.isPresent()){
            JobDescription jobDescription = modelMapper.map(dto, JobDescription.class);
            jobDescription.setTitle(dto.getTitle());
            jobDescription.setDescription(dto.getDescription());
            JobDescription saved = jobDescriptionRepository.save(jobDescription);
            Integer jobId = saved.getId();
            List<AtsDto> atsList = evaluateService.ListAts(cvId,jobId,dto);
            JobDescriptionViewDto jobDescriptionViewDto = jobDescriptionMapper.mapEntityToDto(jobDescription);
            jobDescriptionViewDto.setAts(atsList);
            Cv cv = cvOptional.get();
            Optional<JobDescription> jobDescriptionOptional = jobDescriptionRepository.findById(jobId);
            if(jobDescriptionOptional.isPresent()){
                JobDescription jobDescription1 = jobDescriptionOptional.get();
                cv.setJobDescription(jobDescription1);
            }
            cvRepository.save(cv);
            return jobDescriptionViewDto;
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
    public JobDescriptionViewDto updateJobDescription(Integer cvId, JobDescriptionDto dto) throws Exception {
        Optional<Cv> cvOptional = cvRepository.findById(cvId);
        if(cvOptional.isPresent()){
            Cv cv = cvOptional.get();
            Optional<JobDescription> jobDescriptionOptional = jobDescriptionRepository.findById(cv.getJobDescription().getId());
            List<AtsDto> atsList;
            JobDescriptionViewDto jobDescriptionViewDto = new JobDescriptionViewDto();
            if (jobDescriptionOptional.isPresent()) {
                JobDescription jobDescription = jobDescriptionOptional.get();

                if(jobDescription.getTitle().equals(dto.getTitle())){
                    jobDescription.setTitle(jobDescription.getTitle());
                }else{
                    jobDescription.setTitle(dto.getTitle());
                }
                if(dto.getDescription()!= null){
                    if(isSubstringInString(jobDescription.getDescription(),dto.getDescription())){
                        jobDescription.setDescription(jobDescription.getDescription());
                        atsList = evaluateService.getAts(cvId,jobDescription.getId());
                    }else{
                        jobDescription.setDescription(dto.getDescription());
                        atsRepository.deleteByJobDescriptionId(jobDescription.getId());
                        atsList = evaluateService.ListAts(cvId,jobDescription.getId(),dto);
                    }
                }else{
                    atsList = evaluateService.getAts(cvId,jobDescription.getId());
                }
                JobDescription updatedJobDescription = jobDescriptionRepository.save(jobDescription);
                jobDescriptionViewDto.setTitle(updatedJobDescription.getTitle());
                jobDescriptionViewDto.setDescription(updatedJobDescription.getDescription());
                jobDescriptionViewDto.setAts(atsList);

                return jobDescriptionViewDto;
            } else {
                throw new Exception("Job description with ID " + cv.getJobDescription().getId() + " not found.");
            }
        }else{
            throw new BadRequestException("Cv ID not found");
        }
    }

    public static boolean isSubstringInString(String fullString, String substring) {
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
}
