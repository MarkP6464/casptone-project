package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.NoteDto;
import com.example.capstoneproject.Dto.responses.ApplicationLogResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ApplicationLogService {
    boolean applyCvToPost(Integer userId, Integer cvId, Integer coverLetterId, Integer postingId, NoteDto dto) throws JsonProcessingException;

    List<ApplicationLogResponse> getAll(Integer postId);
}
