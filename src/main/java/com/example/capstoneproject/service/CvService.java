package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.CvAddNewDto;
import com.example.capstoneproject.Dto.CvDto;
import com.example.capstoneproject.Dto.CvUpdateSumDto;
import com.example.capstoneproject.Dto.EducationDto;
import com.example.capstoneproject.entity.Cv;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CvService extends BaseService<CvDto, Integer> {

    CvAddNewDto createCv(Integer id, CvAddNewDto dto);
    List<CvDto> GetCvsById(int customerId);
    CvDto GetCvsByCvId(int customerId,int cvId);
    Cv getCvById(int cvId);
    void deleteCvById(Integer customer,Integer id);

    boolean updateCvSummary(int customerId, int cvId, CvUpdateSumDto dto);
    boolean updateCvContent(int customerId, int cvId, CvAddNewDto dto);
    boolean updateCvContact(int customerId, int cvId, int contactId);
    boolean updateCvTemplate(int customerId, int cvId, int templateId);
}
