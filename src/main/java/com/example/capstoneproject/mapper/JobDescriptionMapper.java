package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.JobDescriptionViewDto;
import com.example.capstoneproject.entity.JobDescription;
import org.springframework.stereotype.Component;

@Component
public class JobDescriptionMapper extends AbstractMapper<JobDescription, JobDescriptionViewDto> {
    public JobDescriptionMapper() {
        super(JobDescription.class, JobDescriptionViewDto.class);
    }
}
