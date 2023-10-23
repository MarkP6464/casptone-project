package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.CoverLetterDto;
import com.example.capstoneproject.entity.CoverLetter;
import org.springframework.stereotype.Component;

@Component
public class CoverLetterMapper extends AbstractMapper<CoverLetter, CoverLetterDto> {
    public CoverLetterMapper() {
        super(CoverLetter.class, CoverLetterDto.class);
    }
}
