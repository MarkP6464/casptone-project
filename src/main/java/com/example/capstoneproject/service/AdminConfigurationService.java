package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.responses.AdminConfigurationApiResponse;
import com.example.capstoneproject.Dto.responses.AdminConfigurationRatioResponse;
import com.example.capstoneproject.Dto.responses.AdminConfigurationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

@Service
public interface AdminConfigurationService {
    AdminConfigurationResponse getByAdminId(Integer id) throws JsonProcessingException;

    AdminConfigurationRatioResponse update(AdminConfigurationRatioResponse dto) throws JsonProcessingException;

    AdminConfigurationApiResponse updateApi(AdminConfigurationApiResponse dto);
}
