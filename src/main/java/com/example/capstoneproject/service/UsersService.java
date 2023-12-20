package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.UsersDto;
import com.example.capstoneproject.Dto.UsersViewDto;
import com.example.capstoneproject.Dto.responses.UserJobTitleViewDto;
import com.example.capstoneproject.Dto.responses.UserManageViewDto;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.RoleType;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UsersService extends BaseService<UsersDto, Integer> {
    Users getUsersById(int UsersId);

    UsersViewDto getContactById(int UsersId);

    UsersDto findByIdAndRole_RoleName(Integer userId);

    List<UserJobTitleViewDto> getJobTitleUser(Integer userId) throws JsonProcessingException;

    List<UserManageViewDto> manageUser(Integer adminId, String name);

    String banUser(Integer adminId, Integer userId);

    String unBanUser(Integer adminId, Integer userId);

    boolean checkEmail(String email);

}
