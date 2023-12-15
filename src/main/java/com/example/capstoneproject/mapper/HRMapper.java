package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.HRDto;
import com.example.capstoneproject.Dto.request.HRBankRequest;
import com.example.capstoneproject.Dto.responses.HRResponse;
import com.example.capstoneproject.entity.HR;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HRMapper {
    @Autowired
    ModelMapper modelMapper;

    public HRDto toDto(HR hr){
        return modelMapper.map(hr, HRDto.class);
    }

    public HR toEntity(HRResponse dto, HR entity){
        modelMapper.map(dto, entity);
        return entity;
    }

}
