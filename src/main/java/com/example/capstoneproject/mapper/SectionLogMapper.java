package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.SectionLogDto;
import com.example.capstoneproject.entity.SectionLog;
import org.springframework.stereotype.Component;

@Component
public class SectionLogMapper extends AbstractMapper<SectionLog, SectionLogDto> {
    public SectionLogMapper() {
        super(SectionLog.class, SectionLogDto.class);
    }
}
