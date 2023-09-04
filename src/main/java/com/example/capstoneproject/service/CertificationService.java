package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.CertificationDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CertificationService extends BaseService<CertificationDto, Integer> {
    CertificationDto update(Integer id, CertificationDto dto);

}
