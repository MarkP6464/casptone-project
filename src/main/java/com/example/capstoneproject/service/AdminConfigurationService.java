package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.responses.AdminConfigurationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public interface AdminConfigurationService {
    AdminConfigurationResponse getByAdminId(Integer id) throws JsonProcessingException;

    AdminConfigurationResponse update(AdminConfigurationResponse dto) throws JsonProcessingException;
}
