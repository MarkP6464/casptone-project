package com.example.capstoneproject.utils;

import com.example.capstoneproject.Dto.UsersDto;
import com.example.capstoneproject.Dto.UsersViewDto;
import com.example.capstoneproject.entity.HR;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.exception.ForbiddenException;
import com.example.capstoneproject.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

    public static UsersViewDto getLoginUser(){
        Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
        return authentication != null? (UsersViewDto) authentication.getPrincipal() : null;
    }

}
