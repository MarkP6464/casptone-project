package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.SectionDto;
import com.example.capstoneproject.entity.Section;
import org.springframework.stereotype.Component;

@Component
public class SectionMapper extends AbstractMapper<Section, SectionDto> {
    public SectionMapper() {
        super(Section.class, SectionDto.class);
    }
}
