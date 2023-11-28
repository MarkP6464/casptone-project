package com.example.capstoneproject.utils;

import com.example.capstoneproject.Dto.UserViewLoginDto;
import com.example.capstoneproject.Dto.UsersViewDto;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

@Component
public class SecurityUtil {

    @Autowired
    UsersRepository usersRepository;

    public UserViewLoginDto getLoginUser(Principal principal){
        JwtAuthenticationToken jwt = (JwtAuthenticationToken) principal;
        String email = jwt.getToken().getClaimAsString("email");
        UserViewLoginDto userViewLoginDto = new UserViewLoginDto();
        Optional<Users> usersOptional = usersRepository.findByEmail(email);
        if(usersOptional.isPresent()){
            Users users = usersOptional.get();
            userViewLoginDto.setId(users.getId());
            return userViewLoginDto;
        }else {
            throw new BadRequestException("Invalid token.");
        }

    }

}
