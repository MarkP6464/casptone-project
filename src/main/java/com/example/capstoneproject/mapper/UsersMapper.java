package com.example.capstoneproject.mapper;

import com.example.capstoneproject.Dto.UsersDto;
import com.example.capstoneproject.Dto.UsersViewDto;
import com.example.capstoneproject.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class UsersMapper extends AbstractMapper<Users, UsersDto> {
    public UsersMapper() {
        super(Users.class, UsersDto.class);
    }

    public UsersViewDto toView(Users users){
        return modelMapper.map(users, UsersViewDto.class);
    }
    public Users toEntity(UsersViewDto usersViewDto){
        return modelMapper.map(usersViewDto, Users.class);
    }
}
