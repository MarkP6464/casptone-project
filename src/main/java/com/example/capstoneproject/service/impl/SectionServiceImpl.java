package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.SectionDto;
import com.example.capstoneproject.entity.Section;
import com.example.capstoneproject.mapper.SectionMapper;
import com.example.capstoneproject.repository.SectionRepository;
import com.example.capstoneproject.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SectionServiceImpl extends AbstractBaseService<Section, SectionDto, Integer> implements SectionService {

    @Autowired
    SectionRepository sectionRepository;
    @Autowired
    SectionMapper sectionMapper;


    public SectionServiceImpl(SectionRepository sectionRepository, SectionMapper sectionMapper) {
        super(sectionRepository, sectionMapper, sectionRepository::findById);
        this.sectionRepository = sectionRepository;
        this.sectionMapper = sectionMapper;
    }
}
