package com.example.capstoneproject.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public interface ApplicationLogService {
    boolean applyCvToPost(Integer userId, Integer cvId, Integer coverLetterId, Integer postingId) throws JsonProcessingException;

}
