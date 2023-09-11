package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.CertificationOfCvDto;
import com.example.capstoneproject.entity.CertificationOfCv;
import org.springframework.stereotype.Component;

@Component
public class CertificationOfCvMapper extends AbstractMapper<CertificationOfCv, CertificationOfCvDto> {
    public CertificationOfCvMapper() {
        super(CertificationOfCv.class, CertificationOfCvDto.class);
    }
}
