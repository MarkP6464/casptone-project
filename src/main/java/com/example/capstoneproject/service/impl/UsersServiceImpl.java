package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.UsersDto;
import com.example.capstoneproject.Dto.UsersViewDto;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.mapper.UsersMapper;
import com.example.capstoneproject.repository.UsersRepository;
import com.example.capstoneproject.service.UsersService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

@Service
public class UsersServiceImpl extends AbstractBaseService<Users, UsersDto, Integer> implements UsersService {

    @Autowired
    UsersRepository UsersRepository;

    @Autowired
    UsersMapper UsersMapper;

    @Autowired
    ModelMapper modelMapper;

    public UsersServiceImpl(UsersRepository UsersRepository, UsersMapper UsersMapper) {
        super(UsersRepository, UsersMapper, UsersRepository::findById);
        this.UsersRepository = UsersRepository;
        this.UsersMapper = UsersMapper;
    }

    @Override
    public Users getUsersById(int usersId) {
        Optional<Users> UsersOptional = UsersRepository.findUsersById(usersId);
        if (UsersOptional.isPresent()) {
            UsersOptional.get().getCvs();
            return UsersOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Users not found with ID: " + usersId);
        }
    }

    @Override
    public UsersViewDto getContactById(int UsersId) {
        Users users = UsersRepository.findUsersById(UsersId).get();
        UsersViewDto UsersViewDto = new UsersViewDto();
        if (Objects.nonNull(users)) {
            UsersViewDto.setId(users.getId());
            UsersViewDto.setName(users.getName());
            UsersViewDto.setAvatar(users.getAvatar());
            UsersViewDto.setPhone(users.getPhone());
            UsersViewDto.setPermissionWebsite(users.getPersonalWebsite());
            UsersViewDto.setEmail(users.getEmail());
            UsersViewDto.setLinkin(users.getLinkin());
            UsersViewDto.setCountry(users.getCountry());
        }
        return UsersViewDto;
    }

    @Override
    public UsersDto findByIdAndRole_RoleName(Integer userId) {
        Optional<Users> usersOptional = UsersRepository.findUsersById(userId);
        if (usersOptional.isPresent()){
            return modelMapper.map(usersOptional.get(), UsersDto.class);
        }else {
            return null;
        }
    }
}
