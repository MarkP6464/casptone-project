package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.Cv;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CvService extends BaseService<CvDto, Integer> {

    CvAddNewDto createCv(Integer id, CvAddNewDto dto);
    List<CvDto> GetCvsById(int UsersId);
    CvDto GetCvsByCvId(int UsersId,int cvId) throws JsonProcessingException;
    Cv getCvById(int cvId);
    void deleteCvById(Integer Users,Integer id);

    boolean updateCvSummary(int UsersId, int cvId, CvUpdateSumDto dto);

    boolean updateCvBody(int UsersId, int cvId, CvBodyDto dto) throws JsonProcessingException;

    boolean updateCvContent(int UsersId, int cvId, CvAddNewDto dto);
    boolean updateCvContact(int UsersId, int cvId, int contactId);
    boolean updateCvTemplate(int UsersId, int cvId, int templateId);

    CvBodyDto getCvBody(int usersId) throws JsonProcessingException;

    Cv synchUp(int cvId) throws JsonProcessingException;
}
