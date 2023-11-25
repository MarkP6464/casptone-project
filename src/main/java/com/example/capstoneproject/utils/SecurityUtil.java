package com.example.capstoneproject.utils;

import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.exception.ForbiddenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

    public static Authentication getLoginUser(){
        Authentication authentication =SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof Users){
            return authentication;
        } else {
            throw new ForbiddenException("Please do authentication");
        }
    }

}
