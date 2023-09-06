package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.CvDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CvService extends BaseService<CvDto, Integer> {
    CvDto update(Integer id, CvDto dto);
    List<CvDto> GetCvsById(int customerId);
}
