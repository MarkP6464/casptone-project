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
    List<CvDto> GetCvsById(int UsersId);
    CvDto GetCvsByCvId(int UsersId,int cvId);
    Cv getCvById(int cvId);
    void deleteCvById(Integer Users,Integer id);

    boolean updateCvSummary(int UsersId, int cvId, CvUpdateSumDto dto);
    boolean updateCvContent(int UsersId, int cvId, CvAddNewDto dto);
    boolean updateCvContact(int UsersId, int cvId, int contactId);
    boolean updateCvTemplate(int UsersId, int cvId, int templateId);
}
