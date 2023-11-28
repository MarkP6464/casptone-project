package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.CvViewDto;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.RoleType;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CvService {

    CvAddNewDto createCv(Integer id, CvBodyDto dto) throws JsonProcessingException;

    CvDto duplicateCv(Integer userId, Integer cvId) throws JsonProcessingException;

    List<CvViewDto> GetCvsById(Integer UsersId, String content);

    CvAddNewDto GetCvByCvId(int UsersId, int cvId) throws JsonProcessingException;

    Cv getCvById(int cvId);

    CvAddNewDto finishUp(int cvId) throws JsonProcessingException;

    void deleteCvById(Integer Users, Integer id);

    boolean updateCvSummary(int UsersId, int cvId, CvUpdateSumDto dto);

    boolean updateCvBody(int cvId, CvBodyDto dto) throws JsonProcessingException;

//    boolean updateCvContent(int UsersId, int cvId, CvAddNewDto dto);

    UsersViewDto updateCvContact(int UsersId, UsersViewDto dto);

//    boolean updateCvTemplate(int UsersId, int cvId, int templateId);

    CvBodyDto getCvBody(int usersId) throws JsonProcessingException;

    CvDto synchUp(int cvId) throws JsonProcessingException;

    List<ScoreDto> getEvaluateCv(int userId, int cvId) throws JsonProcessingException;

    Cv findByUser_IdAndId(Integer UsersId, Integer cvId);

    boolean searchable(Integer userId, Integer cvId);

    List<CvAddNewDto> getListSearchable(String field);

    List<CvResumeDto> getListResume(Integer userId);

    List<ExperienceRoleDto> getListExperienceRole(Integer userId, Integer cvId) throws JsonProcessingException;


}
