package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.InvolvementDto;
import com.example.capstoneproject.entity.Involvement;
import org.springframework.stereotype.Component;

@Component
public class InvolvementMapper extends AbstractMapper<Involvement, InvolvementDto> {
    public InvolvementMapper() {
        super(Involvement.class, InvolvementDto.class);
    }
}
