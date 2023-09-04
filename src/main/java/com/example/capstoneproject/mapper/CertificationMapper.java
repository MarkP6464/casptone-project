package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.CertificationDto;
import com.example.capstoneproject.entity.Certification;
import org.springframework.stereotype.Component;

@Component
public class CertificationMapper extends AbstractMapper<Certification, CertificationDto> {
    public CertificationMapper() {
        super(Certification.class, CertificationDto.class);
    }
}
