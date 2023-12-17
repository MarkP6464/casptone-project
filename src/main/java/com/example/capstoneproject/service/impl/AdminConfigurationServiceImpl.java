package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.responses.AdminConfigurationApiResponse;
import com.example.capstoneproject.Dto.responses.AdminConfigurationRatioResponse;
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
    public AdminConfigurationRatioResponse update(AdminConfigurationRatioResponse dto) throws JsonProcessingException {
        AdminConfigurationRatioResponse adminConfigurationRatioResponse = new AdminConfigurationRatioResponse();
        Users user = usersRepository.findUsersById(1).get();
        if (Objects.nonNull(user)){
            if (user instanceof Admin) {
                Admin admin = (Admin) user;
                AdminConfigurationResponse adminConfigurationResponse = admin.getConfiguration();
                modelMapper.map(dto, adminConfigurationResponse);
                admin.setConfiguration(adminConfigurationResponse);
                Admin admin1 = usersRepository.save(admin);
                adminConfigurationRatioResponse.setMoneyRatio(admin1.getConfiguration().getMoneyRatio());
                adminConfigurationRatioResponse.setVipYearRatio(admin1.getConfiguration().getVipYearRatio());
                adminConfigurationRatioResponse.setVipMonthRatio(admin1.getConfiguration().getVipMonthRatio());
                return  adminConfigurationRatioResponse;
            }
        }
        throw new BadRequestException("id not valid to token");
    }

    @Override
    public AdminConfigurationApiResponse updateApi(AdminConfigurationApiResponse dto) {
        AdminConfigurationApiResponse adminConfigurationApiResponse = new AdminConfigurationApiResponse();
        Users user = usersRepository.findUsersById(1).get();
        if (Objects.nonNull(user)){
            if (user instanceof Admin) {
                Admin admin = (Admin) user;
                AdminConfigurationResponse adminConfigurationResponse = admin.getConfiguration();
                modelMapper.map(dto, adminConfigurationResponse);
                admin.setConfiguration(adminConfigurationResponse);
                usersRepository.save(admin);
                Admin admin1 = usersRepository.save(admin);
                adminConfigurationApiResponse.setApiKey(admin1.getConfiguration().getApiKey());
                return  adminConfigurationApiResponse;
            }
        }
        throw new BadRequestException("id not valid to token");
    }

//    @Override
//    public AdminConfigurationRatioResponse update(AdminConfigurationRatioResponse dto) throws JsonProcessingException {
//        Users user = usersRepository.findUsersById(1).get();
//        if (Objects.nonNull(user)){
//            if (user instanceof Admin) {
//                Admin admin = (Admin) user;
//                AdminConfigurationResponse adminConfigurationResponse = admin.getConfiguration();
//                modelMapper.map(dto, adminConfigurationResponse);
//                admin.setConfiguration(adminConfigurationResponse);
//                usersRepository.save(admin);
////                return  adminConfigurationResponse;
//            }
//        }
//        throw new BadRequestException("id not valid to token");
//    }
}
