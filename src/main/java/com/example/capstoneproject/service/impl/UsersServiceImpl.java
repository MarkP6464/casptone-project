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
import com.example.capstoneproject.enums.TransactionStatus;
import com.example.capstoneproject.enums.TransactionType;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.mapper.UsersMapper;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.repository.TransactionRepository;
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
    TransactionRepository transactionRepository;

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
    public List<UserManageViewDto> manageUser(Integer adminId, String search) {
        List<Users> users = UsersRepository.findAllByStatusAndIdNot(BasicStatus.ACTIVE, adminId);
        List<UserManageViewDto> userManages = new ArrayList<>();

        if (users != null && !users.isEmpty()) {
            userManages = users.stream()
                    .filter(user -> search == null || user.getName().toLowerCase().contains(search.toLowerCase()))
                    .map(user -> {
                        UserManageViewDto userManageViewDto = new UserManageViewDto();
                        userManageViewDto.setId(user.getId());
                        userManageViewDto.setName(user.getName());
                        userManageViewDto.setStatus(user.isBan() ? "Banned" : "UnBanned");
                        userManageViewDto.setLastActive(user.getLastActive());
                        Double money = transactionRepository.sumExpenditureByUserIdAndTransactionTypeAndStatus(user.getId(), TransactionType.ADD, TransactionStatus.SUCCESSFUL);
                        Long mon = (money != null) ? money.longValue() : 0L;
                        userManageViewDto.setMoney(formatPrice(mon));
                        userManageViewDto.setRole(user.getRole().getRoleName());
                        return userManageViewDto;
                    })
                    .collect(Collectors.toList());
        }

        return userManages;
    }


    @Override
    public String banUser(Integer adminId, Integer userId) {
        Optional<Users> adminOptional = UsersRepository.findByIdAndRole_RoleName(adminId, RoleType.ADMIN);
        if(adminOptional.isPresent()){
            Optional<Users> usersOptional = UsersRepository.findUsersById(userId);
            if(usersOptional.isPresent()){
                Users users = usersOptional.get();
                users.setBan(true);
                UsersRepository.save(users);
                return "Banned user successful.";
            }else{
                throw new BadRequestException("User not exist.");
            }
        }else{
            throw new BadRequestException("Please login with role admin.");
        }
    }

    @Override
    public String unBanUser(Integer adminId, Integer userId) {
        Optional<Users> adminOptional = UsersRepository.findByIdAndRole_RoleName(adminId, RoleType.ADMIN);
        if(adminOptional.isPresent()){
            Optional<Users> usersOptional = UsersRepository.findUsersById(userId);
            if(usersOptional.isPresent()){
                Users users = usersOptional.get();
                users.setBan(false);
                UsersRepository.save(users);
                return "Un-Banned user successful.";
            }else{
                throw new BadRequestException("User not exist.");
            }
        }else{
            throw new BadRequestException("Please login with role admin.");
        }
    }

    @Override
    public boolean checkEmail(String email) {
        Optional<Users> usersOptional = UsersRepository.findByEmail(email);
        if(usersOptional.isPresent()){
            return true;
        }else{
            return false;
        }
    }

    public static String formatPrice(long price) {
        String priceStr = String.valueOf(price);

        int length = priceStr.length();
        StringBuilder formattedPrice = new StringBuilder();

        for (int i = length - 1; i >= 0; i--) {
            formattedPrice.insert(0, priceStr.charAt(i));

            // Insert a dot after every 3 digits, but not at the beginning
            if ((length - i) % 3 == 0 && i != 0) {
                formattedPrice.insert(0, ".");
            }
        }

        return formattedPrice.toString();
    }

}
