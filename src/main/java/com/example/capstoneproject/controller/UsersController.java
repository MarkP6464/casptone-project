package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.UsersDto;
import com.example.capstoneproject.Dto.UsersViewDto;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.mapper.UsersMapper;
import com.example.capstoneproject.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UsersController {

    @Autowired
    UsersService UsersService;

    @Autowired
    UsersMapper usersMapper;

    public UsersController(UsersService UsersService) {
        this.UsersService = UsersService;
    }

    @GetMapping("/user-info/{id}")
    public UsersViewDto getContact(@PathVariable("id") Integer id) {
        return UsersService.getContactById(id);
    }

    @GetMapping("/{userId}")
    public UsersViewDto getAllInfo(@PathVariable("userId") Integer userId) {
        Users user = UsersService.getUsersById(userId);
        return usersMapper.toView(user);
    }

    @GetMapping("/{userId}/micro")
    public UsersDto findByIdAndRoleName(@PathVariable("userId") Integer userId) {
        return UsersService.findByIdAndRole_RoleName(userId);
    }
}
