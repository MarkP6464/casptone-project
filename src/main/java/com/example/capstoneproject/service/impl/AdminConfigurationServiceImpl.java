package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.responses.AdminConfigurationResponse;
import com.example.capstoneproject.entity.Admin;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.repository.UsersRepository;
import com.example.capstoneproject.service.AdminConfigurationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AdminConfigurationServiceImpl implements AdminConfigurationService {
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    ModelMapper modelMapper;

    public Boolean isAdmin(Integer id){
        Users user = usersRepository.findUsersById(id).get();
        if (user instanceof Admin){
            return true;
        }
        return false;
    }

    @Override
    public AdminConfigurationResponse getByAdminId(Integer id) throws JsonProcessingException {
        Users user = usersRepository.findUsersById(1).get();
        if (Objects.nonNull(user)){
            if (user instanceof Admin) {
                Admin admin = (Admin) user;
                return admin.getConfiguration();
            } else throw new BadRequestException("id not valid to token");
        }else throw new BadRequestException("Not found configuration by id");
    }

    @Override
    public AdminConfigurationResponse update(AdminConfigurationResponse dto) throws JsonProcessingException {
        Users user = usersRepository.findUsersById(1).get();
        if (Objects.nonNull(user)){
            if (user instanceof Admin) {
                Admin admin = (Admin) user;
                AdminConfigurationResponse adminConfigurationResponse = admin.getConfiguration();
                modelMapper.map(dto, adminConfigurationResponse);
                admin.setConfiguration(adminConfigurationResponse);
                usersRepository.save(admin);
                return  adminConfigurationResponse;
            }
        }
        throw new BadRequestException("id not valid to token");
    }
}
