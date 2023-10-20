package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.AtsDto;
import com.example.capstoneproject.Dto.JobDescriptionDto;
import com.example.capstoneproject.Dto.JobDescriptionViewDto;
import com.example.capstoneproject.entity.*;
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
        JobDescription jobDescription = modelMapper.map(dto, JobDescription.class);
        jobDescription.setTitle(dto.getTitle());
        jobDescription.setDescription(dto.getDescription());
        JobDescription saved = jobDescriptionRepository.save(jobDescription);
        Integer jobId = saved.getId();
        List<AtsDto> atsList = evaluateService.ListAts(cvId,jobId,dto);
        JobDescriptionViewDto jobDescriptionViewDto = jobDescriptionMapper.mapEntityToDto(jobDescription);
        jobDescriptionViewDto.setAts(atsList);
        return jobDescriptionViewDto;
    }

    @Override
    public JobDescriptionViewDto getJobDescription(Integer cvId, Integer jobId) throws JsonProcessingException {
        Optional<JobDescription> jobDescription = jobDescriptionRepository.findById(jobId);
        JobDescriptionViewDto jobDescriptionViewDto = new JobDescriptionViewDto();
        if(jobDescription.isPresent()){
            jobDescriptionViewDto.setTitle(jobDescription.get().getTitle());
            jobDescriptionViewDto.setDescription(jobDescription.get().getDescription());
            List<AtsDto> atsList = evaluateService.getAts(cvId,jobId);
            jobDescriptionViewDto.setAts(atsList);

        }
        return jobDescriptionViewDto;
    }

    @Override
    public JobDescriptionViewDto updateJobDescription(Integer cvId, Integer jobId, JobDescriptionDto dto) throws Exception {
        Optional<JobDescription> jobDescriptionOptional = jobDescriptionRepository.findById(jobId);
        List<AtsDto> atsList = new ArrayList<>();
        JobDescriptionViewDto jobDescriptionViewDto = new JobDescriptionViewDto();
        if (jobDescriptionOptional.isPresent()) {
            JobDescription jobDescription = jobDescriptionOptional.get();

            if(jobDescription.getTitle().equals(dto.getTitle())){
                jobDescription.setTitle(jobDescription.getTitle());
            }else{
                jobDescription.setTitle(dto.getTitle());
            }
            if(jobDescription.getDescription().equalsIgnoreCase(dto.getDescription())){
                jobDescription.setDescription(jobDescription.getDescription());
                atsList = evaluateService.getAts(cvId,jobId);
            }else{
                jobDescription.setDescription(dto.getDescription());
                atsRepository.deleteByJobDescriptionId(jobId);
                atsList = evaluateService.ListAts(cvId,jobId,dto);
            }
            JobDescription updatedJobDescription = jobDescriptionRepository.save(jobDescription);
            jobDescriptionViewDto.setTitle(updatedJobDescription.getTitle());
            jobDescriptionViewDto.setDescription(updatedJobDescription.getDescription());
            jobDescriptionViewDto.setAts(atsList);

            return jobDescriptionMapper.mapEntityToDto(updatedJobDescription);
        } else {
            throw new Exception("Job description with ID " + jobId + " not found.");
        }
    }
}
