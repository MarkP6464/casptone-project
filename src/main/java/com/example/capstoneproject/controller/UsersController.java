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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UsersController {

    @Autowired
    UsersService UsersService;

    @Autowired
    UsersMapper usersMapper;

    public UsersController(UsersService UsersService) {
        this.UsersService = UsersService;
    }

    @GetMapping("/user/user-info/{id}")
    public UsersViewDto getContact(@PathVariable("id") Integer id) {
        return UsersService.getContactById(id);
    }

    @GetMapping("/user/{user-id}")
    public UsersViewDto getAllInfo(@PathVariable("user-id") Integer userId) {
        Users user = UsersService.getUsersById(userId);
        return usersMapper.toView(user);
    }

    @GetMapping("/user/{user-id}/job-title/company/config")
    public ResponseEntity<?> getJobTitleInfo(@PathVariable("user-id") Integer userId) throws JsonProcessingException {
        return ResponseEntity.ok(UsersService.getJobTitleUser(userId));
    }

    @GetMapping("/user/{user-id}/micro")
    public UsersDto findByIdAndRoleName(@PathVariable("user-id") Integer userId) {
        return UsersService.findByIdAndRole_RoleName(userId);
    }

    @GetMapping("admin/{admin-id}/manage/user/information")
    @PreAuthorize("hasAuthority('read:admin')")
    public ResponseEntity<?> manageUser(@PathVariable("admin-id") Integer adminId, @RequestParam(required = false) String name) {
        return ResponseEntity.ok(UsersService.manageUser(adminId, name));
    }

    @PutMapping("admin/{admin-id}/manage/user/{user-id}/ban")
    @PreAuthorize("hasAuthority('update:admin')")
    public ResponseEntity<?> banUser(@PathVariable("admin-id") Integer adminId, @PathVariable("user-id") Integer userId) {
        return ResponseEntity.ok(UsersService.banUser(adminId,userId));
    }

    @PutMapping("admin/{admin-id}/manage/user/{user-id}/un-ban")
    @PreAuthorize("hasAuthority('update:admin')")
    public ResponseEntity<?> unBanUser(@PathVariable("admin-id") Integer adminId, @PathVariable("user-id") Integer userId) {
        return ResponseEntity.ok(UsersService.unBanUser(adminId,userId));
    }
}
