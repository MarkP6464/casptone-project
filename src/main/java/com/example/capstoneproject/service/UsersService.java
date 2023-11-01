package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.UsersDto;
import com.example.capstoneproject.Dto.UsersViewDto;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.RoleType;
import org.springframework.stereotype.Service;

public interface UsersService extends BaseService<UsersDto, Integer> {
    Users getUsersById(int UsersId);

    UsersViewDto getContactById(int UsersId);

    UsersDto findByIdAndRole_RoleName(Integer userId);

}
