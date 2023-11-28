package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.AdminConfigurationDto;
import com.example.capstoneproject.Dto.HRDto;
import com.example.capstoneproject.Dto.responses.AdminConfigurationResponse;
import com.example.capstoneproject.Dto.responses.HRResponse;
import com.example.capstoneproject.entity.AdminConfiguration;
import com.example.capstoneproject.entity.HR;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminConfigurationMapper {
    @Autowired
    ModelMapper modelMapper;

    public AdminConfigurationResponse toDto(AdminConfiguration hr){
        return modelMapper.map(hr, AdminConfigurationResponse.class);
    }

    public AdminConfiguration toEntity(AdminConfigurationResponse dto, AdminConfiguration entity){
        modelMapper.map(dto, entity);
        return entity;
    }
}
