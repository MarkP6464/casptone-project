package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.HRDto;
import com.example.capstoneproject.Dto.TransactionDto;
import com.example.capstoneproject.Dto.request.HRBankRequest;
import com.example.capstoneproject.Dto.responses.AdminConfigurationResponse;
import com.example.capstoneproject.Dto.responses.HRResponse;
import com.example.capstoneproject.Dto.responses.TransactionResponse;
import com.example.capstoneproject.entity.HR;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.MoneyType;
import com.example.capstoneproject.enums.TransactionType;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.exception.ForbiddenException;
import com.example.capstoneproject.exception.InternalServerException;
import com.example.capstoneproject.mapper.HRMapper;
import com.example.capstoneproject.repository.HRRepository;
import com.example.capstoneproject.repository.UsersRepository;
import com.example.capstoneproject.service.AdminConfigurationService;
import com.example.capstoneproject.service.HRService;
import com.example.capstoneproject.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class HRServiceImpl implements HRService {
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    HRRepository hrRepository;

    @Autowired
    HRMapper hrMapper;
    @Autowired
    TransactionService transactionService;
    @Autowired
    AdminConfigurationService adminConfigurationService;

    @Override
    public Boolean checkVip(Integer id){
        Users user = usersRepository.findUsersById(id).get();
        if (Objects.nonNull(user)){
            if (user instanceof HR){
                HR hr = (HR) user;
                if (Objects.nonNull(hr.getVip())){
                    if (hr.getVip() == Boolean.TRUE){
                        return true;
                    } throw new BadRequestException("Please buy subscription to buy this feature!");
                } else throw new InternalServerException("Vip is null");
            }
        }
        throw new BadRequestException("Not found HR by id");
    }

    @Override
    public HRDto get(Integer id){
        Users user = usersRepository.findUsersById(id).get();
        if (Objects.nonNull(user)){
            if (user instanceof HR){
                HR hr = (HR) user;
                if (Objects.nonNull(hr)){
                    return hrMapper.toDto(hr);
                }else {
                    throw new BadRequestException("HR Not found");
                }
            }
        }
        throw new BadRequestException("Not found HR by id");
    }

    @Override
    public HRDto update(HRResponse dto){
        Users users = usersRepository.findUsersById(dto.getId()).get();
        if (Objects.nonNull(users)){
            if (users instanceof HR){
                HR hr = (HR) users;
                hrMapper.toEntity(dto, hr);
                return hrMapper.toDto(hrRepository.save(hr));
            }
        }
        throw new BadRequestException("user not found");
    }

    @Override
    public String register(TransactionResponse dto) throws Exception {
        Users users = usersRepository.findUsersById(dto.getUserId()).get();
        Double expenditure = dto.getExpenditure();
        if (Objects.nonNull(users)) {
            if (users instanceof HR) {
                HR hr = (HR) users;
                TransactionDto transactionDto = new TransactionDto();
                AdminConfigurationResponse adminConfigurationDto = adminConfigurationService.getByAdminId(1);
                if (adminConfigurationDto.getVipMonthRatio().equals(expenditure) || adminConfigurationDto.getVipYearRatio().equals(expenditure)) {
                    if (adminConfigurationDto.getVipMonthRatio().equals(expenditure)) {
                            transactionDto.setResponseMessage("Extend 1 month subscription");
                            transactionDto.setExpenditure(expenditure);
                            transactionDto.setTransactionType(TransactionType.ADD);
                            transactionDto.setMoneyType(MoneyType.SUBSCRIPTION);
                    }else if (adminConfigurationDto.getVipYearRatio().equals(expenditure)) {
                            transactionDto.setResponseMessage("Extend 1 year subscription");
                            transactionDto.setExpenditure(expenditure);
                            transactionDto.setTransactionType(TransactionType.ADD);
                            transactionDto.setMoneyType(MoneyType.SUBSCRIPTION);
                    }
                    transactionDto.setUserId(dto.getUserId());
                    String s = transactionService.create(transactionDto);
                    return s;
                } else {
                    throw new BadRequestException("Not a valid register subscription plan");
                }
            } else {
                throw new ForbiddenException("You are not HR!");
            }
        } else throw new BadRequestException("Not found user id");
    }
    @Scheduled(cron = "0 0 0 * * * ")
    public void checkSubscription(){
        LocalDate currentDate = LocalDate.now();
        List<HR> hrList = hrRepository.findAllByStatusAndVipTrue(BasicStatus.ACTIVE);
        for (HR hr : hrList) {
            if (currentDate.compareTo(hr.getExpiredDay()) >= 7L){
                ApplicationLogServiceImpl.sendEmail(hr.getEmail(), "CvBuilder subscription is going to expired!"
                        , "Dear user " + hr.getName() + ", your subscription is going to expired on " + hr.getExpiredDay()
                                + "Please extend your subscription. CvBuilder reminder mail!");
            } else if (currentDate.compareTo(hr.getExpiredDay()) == 0L){
                hr.setVip(Boolean.FALSE);
                hrRepository.save(hr);
                ApplicationLogServiceImpl.sendEmail(hr.getEmail(), "CvBuilder subscription expired!"
                        , "Dear user " + hr.getName() + ", your subscription is expired! Thanks for your using.");
            }
        }
    }
}
