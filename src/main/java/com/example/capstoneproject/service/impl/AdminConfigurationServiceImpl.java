package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.AdminConfigurationDto;
import com.example.capstoneproject.Dto.HRDto;
import com.example.capstoneproject.Dto.TransactionDto;
import com.example.capstoneproject.Dto.responses.AdminConfigurationResponse;
import com.example.capstoneproject.Dto.responses.HRResponse;
import com.example.capstoneproject.Dto.responses.TransactionResponse;
import com.example.capstoneproject.constant.PaymentConstant;
import com.example.capstoneproject.entity.Admin;
import com.example.capstoneproject.entity.AdminConfiguration;
import com.example.capstoneproject.entity.HR;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.MoneyType;
import com.example.capstoneproject.enums.TransactionType;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.exception.ForbiddenException;
import com.example.capstoneproject.mapper.AdminConfigurationMapper;
import com.example.capstoneproject.mapper.HRMapper;
import com.example.capstoneproject.repository.AdminConfigurationRepository;
import com.example.capstoneproject.repository.HRRepository;
import com.example.capstoneproject.repository.UsersRepository;
import com.example.capstoneproject.service.AdminConfigurationService;
import com.example.capstoneproject.service.HRService;
import com.example.capstoneproject.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class AdminConfigurationServiceImpl implements AdminConfigurationService {
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    AdminConfigurationRepository adminConfigurationRepository;
    @Autowired
    AdminConfigurationMapper adminConfigurationMapper;

    public Boolean isAdmin(Integer id){
        Users user = usersRepository.findUsersById(id).get();
        if (user instanceof Admin){
            return true;
        }
        return false;
    }

    @Override
    public AdminConfigurationResponse getByAdminId(Integer id){
         AdminConfiguration adminConfiguration = adminConfigurationRepository.findByUser_Id(id);
         if (Objects.nonNull(adminConfiguration)){
             return adminConfigurationMapper.toDto(adminConfiguration);
         }else throw new BadRequestException("Not found configuration by id");
    }

    @Override
    public AdminConfigurationResponse update(AdminConfigurationResponse dto){
        Users users = usersRepository.findUsersById(1).get();
        if (Objects.nonNull(users)){
            AdminConfiguration adminConfiguration = adminConfigurationRepository.findByUser_Id(1);
            if (Objects.nonNull(adminConfiguration)){
                adminConfigurationMapper.toEntity(dto, adminConfiguration);
                return adminConfigurationMapper.toDto(adminConfiguration);
            } else throw new BadRequestException("Not Found adminConfiguration");
        }
        throw new BadRequestException("id not valid to token");
    }
}
