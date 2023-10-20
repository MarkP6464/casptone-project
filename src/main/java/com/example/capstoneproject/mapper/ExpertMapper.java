package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.ExpertDto;
import com.example.capstoneproject.entity.Expert;
import org.springframework.stereotype.Component;

@Component
public class ExpertMapper extends AbstractMapper<Expert, ExpertDto> {
    public ExpertMapper() {
        super(Expert.class, ExpertDto.class);
    }
}
