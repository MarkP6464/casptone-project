package com.example.capstoneproject.controller;

import com.example.capstoneproject.Dto.UsersDto;
import com.example.capstoneproject.Dto.UsersViewDto;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.mapper.UsersMapper;
import com.example.capstoneproject.service.UsersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
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

    @GetMapping("/{user-id}")
    public UsersViewDto getAllInfo(@PathVariable("user-id") Integer userId) {
        Users user = UsersService.getUsersById(userId);
        return usersMapper.toView(user);
    }

    @GetMapping("/{user-id}/job-title/company/config")
    public ResponseEntity<?> getJobTitleInfo(@PathVariable("user-id") Integer userId) throws JsonProcessingException {
        return ResponseEntity.ok(UsersService.getJobTitleUser(userId));
    }

    @GetMapping("/{user-id}/micro")
    public UsersDto findByIdAndRoleName(@PathVariable("user-id") Integer userId) {
        return UsersService.findByIdAndRole_RoleName(userId);
    }

    @GetMapping("manage/customer/information")
    public ResponseEntity<?> manageUser() {
        return ResponseEntity.ok(UsersService.manageUser());
    }
}
