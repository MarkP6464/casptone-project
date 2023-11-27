package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.AdminConfigurationDto;
import com.example.capstoneproject.Dto.AtsDto;
import com.example.capstoneproject.Dto.responses.AdminConfigurationResponse;
import com.example.capstoneproject.entity.Ats;
import org.springframework.stereotype.Service;

@Service
public interface AdminConfigurationService {
    AdminConfigurationResponse getByAdminId(Integer id);

    AdminConfigurationResponse update(AdminConfigurationResponse dto);
}
