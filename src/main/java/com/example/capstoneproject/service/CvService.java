package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.Dto.responses.CvResponse;
import com.example.capstoneproject.Dto.responses.CvViewDto;
import com.example.capstoneproject.Dto.responses.ResumeTitleResponse;
import com.example.capstoneproject.Dto.responses.UsersCvViewDto;
import com.example.capstoneproject.entity.Cv;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

@Service
public interface CvService {

    CvAddNewDto createCv(Integer id, CvBodyDto dto) throws JsonProcessingException;

    CvDto duplicateCv(Integer userId, Integer cvId) throws JsonProcessingException;

    List<CvViewDto> GetCvsById(Integer UsersId, String content);

    CvAddNewDto GetCvByCvId(int UsersId, int cvId) throws JsonProcessingException;

    Cv getCvById(int cvId);

    CvBodyDto finishUp(int cvId) throws JsonProcessingException;

    void deleteCvById(Integer Users, Integer id);

    boolean updateCvSummary(int UsersId, int cvId, CvUpdateSumDto dto) throws JsonProcessingException;

    boolean updateCvBody(int cvId, CvBodyDto dto) throws JsonProcessingException;

    boolean updateCvBodyAndHistory(int cvId, CvBodyDto dto) throws JsonProcessingException;

    UsersCvViewDto updateCvContact(Integer UsersId, Integer cvId, UsersCvViewDto dto) throws JsonProcessingException;

    UsersCvViewDto getCvContact(Integer userId, Integer cvId) throws JsonProcessingException;

    Boolean updateCvTarget(Integer id, CvUpdateDto dto, Principal principal);

    CvUpdateDto getTitleResume(Integer cvId);

    CvBodyDto getCvBody(int usersId) throws JsonProcessingException;

    CvDto synchUp(int cvId) throws JsonProcessingException;

    Cv findByUser_IdAndId(Integer UsersId, Integer cvId);

    boolean searchable(Integer userId, Integer cvId);

    List<CvAddNewDto> getListSearchable(String field);

    List<CvResumeDto> getListResume(Integer userId);

    List<ExperienceRoleDto> getListExperienceRole(Integer userId, Integer cvId) throws JsonProcessingException;

    ChatResponse generateSummaryCV(Integer cvId, SummaryGenerationDto dto, Principal principal) throws JsonProcessingException;

    ChatResponse reviewCV(float temperature, Integer cvId, Principal principal) throws JsonProcessingException;

    ChatResponseArray rewritteExperience(ReWritterExperienceDto dto, Principal principal) throws JsonProcessingException;

    List<CvResponse> listCvDetail(Integer userId);

    CvBodyDto parse(Integer cvId, Integer historyId) throws JsonProcessingException;

    boolean createParse(Integer cvId, CvBodyReviewDto dto) throws JsonProcessingException;

    boolean createOldParse(Integer cvId, CvBodyReviewDto dto) throws JsonProcessingException;

    ResumeTitleResponse getResumeName(Integer cvId);

    void saveAfterFiveMin(HttpServletRequest request, Integer cvId, CvBodyDto dto);

    void saveToHistory(HttpServletRequest request, Integer userId, Integer cvId) throws JsonProcessingException;
}
