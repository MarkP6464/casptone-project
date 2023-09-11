package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.InvolvementOfCvDto;
import com.example.capstoneproject.entity.InvolvementOfCv;
import org.springframework.stereotype.Component;

@Component
public class InvolvementOfCvMapper extends AbstractMapper<InvolvementOfCv, InvolvementOfCvDto> {
    public InvolvementOfCvMapper() {
        super(InvolvementOfCv.class, InvolvementOfCvDto.class);
    }
}
