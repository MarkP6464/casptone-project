package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.CvAddNewDto;
import com.example.capstoneproject.Dto.CvDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CvService extends BaseService<CvDto, Integer> {

    boolean updateCv(Integer id, CvAddNewDto dto);

    CvAddNewDto createCv(CvAddNewDto dto);
    List<CvDto> GetCvsById(int customerId);
    CvDto GetCvsByCvId(int cvId);
}
