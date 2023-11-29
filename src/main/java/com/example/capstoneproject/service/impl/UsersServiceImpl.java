package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.CvBodyDto;
import com.example.capstoneproject.Dto.ExperienceRoleDto;
import com.example.capstoneproject.Dto.UsersDto;
import com.example.capstoneproject.Dto.UsersViewDto;
import com.example.capstoneproject.Dto.responses.UserJobTitleViewDto;
import com.example.capstoneproject.Dto.responses.UserManageViewDto;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.mapper.UsersMapper;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.repository.UsersRepository;
import com.example.capstoneproject.service.UsersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsersServiceImpl extends AbstractBaseService<Users, UsersDto, Integer> implements UsersService {

    @Autowired
    UsersRepository UsersRepository;

    @Autowired
    CvRepository cvRepository;

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
            UsersViewDto.setPersonalWebsite(users.getPersonalWebsite());
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

    @Override
    public List<UserJobTitleViewDto> getJobTitleUser(Integer userId) throws JsonProcessingException {
        List<Cv> cvs = cvRepository.findAllByUsersIdAndStatus(userId, BasicStatus.ACTIVE);
        List<UserJobTitleViewDto> jobTitles = new ArrayList<>();
        if (!cvs.isEmpty()) {
            for (Cv cv : cvs) {
                CvBodyDto cvBodyDto = cv.deserialize();
                cvBodyDto.getExperiences().stream()
//                .filter(x -> x.getIsDisplay())
                        .forEach(x -> {
                            UserJobTitleViewDto experienceRoleDto = new UserJobTitleViewDto();
                            experienceRoleDto.setJobTitle(x.getRole());
                            experienceRoleDto.setCompany(x.getCompanyName());
                            jobTitles.add(experienceRoleDto);
                        });
            }
        }

        return jobTitles;
    }

    @Override
    public List<UserManageViewDto> manageUser() {
        List<Users> users = UsersRepository.findAll();
        List<UserManageViewDto> userManages = new ArrayList<>();
        if(!users.isEmpty()){
            for(Users user: users){
                UserManageViewDto userManageViewDto = new UserManageViewDto();
                userManageViewDto.setId(user.getId());
                userManageViewDto.setName(user.getName());
                userManageViewDto.setAvatar(user.getAvatar());
                userManageViewDto.setPhone(user.getPhone());
                userManageViewDto.setEmail(user.getEmail());
                userManageViewDto.setAccountBalance(user.getAccountBalance());
                userManageViewDto.setRole(user.getRole().getRoleName());
                userManages.add(userManageViewDto);
            }
        }
        return userManages;
    }

}
