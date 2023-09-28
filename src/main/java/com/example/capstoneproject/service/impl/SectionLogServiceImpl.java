package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.SectionLogDto;
import com.example.capstoneproject.entity.SectionLog;
import com.example.capstoneproject.mapper.SectionLogMapper;
import com.example.capstoneproject.repository.SectionLogRepository;
import com.example.capstoneproject.service.SectionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SectionLogServiceImpl extends AbstractBaseService<SectionLog, SectionLogDto, Integer> implements SectionLogService {

    @Autowired
    SectionLogRepository sectionLogRepository;
    @Autowired
    SectionLogMapper sectionLogMapper;


    public SectionLogServiceImpl(SectionLogRepository sectionLogRepository, SectionLogMapper sectionLogMapper) {
        super(sectionLogRepository, sectionLogMapper, sectionLogRepository::findById);
        this.sectionLogRepository = sectionLogRepository;
        this.sectionLogMapper = sectionLogMapper;
    }
}
