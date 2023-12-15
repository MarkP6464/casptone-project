package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.NoteDto;
import com.example.capstoneproject.Dto.responses.ApplicationLogFullResponse;
import com.example.capstoneproject.Dto.responses.ApplicationLogResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.List;

public interface ApplicationLogService {
    boolean applyCvToPost(Integer userId, Integer cvId, Integer coverLetterId, Integer postingId, NoteDto dto) throws JsonProcessingException;

    List<ApplicationLogResponse> getAll(Integer postId);

    List<ApplicationLogFullResponse> getAllByHrID(Integer hrId);

    List<ApplicationLogResponse> getAllByCandidateId(Integer id);

    ApplicationLogResponse updateDownloaded(Integer id);

    ApplicationLogResponse updateSeen(Integer id);
}
