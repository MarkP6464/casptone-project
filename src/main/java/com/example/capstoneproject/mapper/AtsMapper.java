package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.AtsDto;
import com.example.capstoneproject.entity.Ats;
import org.springframework.stereotype.Component;

@Component
public class AtsMapper extends AbstractMapper<Ats, AtsDto> {
    public AtsMapper() {
        super(Ats.class, AtsDto.class);
    }
}
