package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.HRDto;
import com.example.capstoneproject.Dto.TransactionDto;
import com.example.capstoneproject.constant.PaymentConstant;
import com.example.capstoneproject.entity.HR;
import com.example.capstoneproject.entity.ReviewRequest;
import com.example.capstoneproject.entity.Transaction;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.MoneyType;
import com.example.capstoneproject.enums.StatusReview;
import com.example.capstoneproject.enums.TransactionType;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.exception.ForbiddenException;
import com.example.capstoneproject.mapper.HRMapper;
import com.example.capstoneproject.repository.HRRepository;
import com.example.capstoneproject.service.HRService;
import com.example.capstoneproject.service.TransactionService;
import com.example.capstoneproject.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@Service
public class HRServiceImpl implements HRService {

    @Autowired
    HRRepository hrRepository;

    @Autowired
    HRMapper hrMapper;
    @Autowired
    TransactionService transactionService;

    @Override
    public HRDto get(Integer id){
        HR hr = hrRepository.getOne(id);
        if (Objects.nonNull(hr)){
            return hrMapper.toDto(hr);
        }else {
            throw new BadRequestException("HR Not found");
        }
    }

    @Override
    public HRDto update(HRDto dto){
        HR hr = hrMapper.toEntity(dto);
        if (Objects.nonNull(hr)){
            return hrMapper.toDto(hr);
        }else {
            throw new BadRequestException("HR Not found");
        }
    }

    @Override
    public HRDto register(Long expenditure, Long conversionAmount) throws Exception {
        Users users = (Users) SecurityUtil.getLoginUser();
        if (users instanceof HR){
            HR hr = (HR) users;
            TransactionDto transactionDto = new TransactionDto();
            if (PaymentConstant.vipAMonthRatio.equals(expenditure) || PaymentConstant.vipAYearRatio.equals(expenditure)){
                if (PaymentConstant.vipAMonthRatio.equals(expenditure)){
                    if (Long.valueOf(30).equals(conversionAmount)){
                        transactionDto.setResponseMessage("extend 1 month subscription");
                        transactionDto.setExpenditure(expenditure);
                        transactionDto.setTransactionType(TransactionType.ADD);
                        transactionDto.setMoneyType(MoneyType.SUBSCRIPTION);
                    }else if (Long.valueOf(365).equals(conversionAmount)){
                        transactionDto.setResponseMessage("extend 1 year subscription");
                        transactionDto.setExpenditure(expenditure);
                        transactionDto.setTransactionType(TransactionType.ADD);
                        transactionDto.setMoneyType(MoneyType.SUBSCRIPTION);
                    }else {
                        throw new BadRequestException("Not a valid register subscription params");
                    }
                }
                transactionService.create(transactionDto);
            }else {
                throw new BadRequestException("Not a valid register subscription params");
            }
        } else {
            throw new ForbiddenException("You are not HR!");
        }
        return null;
    }
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkSubscription(){
        LocalDate currentDate = LocalDate.now();
        List<HR> hrList = hrRepository.findAllByStatusAndSubscriptionTrue(BasicStatus.ACTIVE.toString());
        for (HR hr : hrList) {
            if (currentDate.isAfter(hr.getExpiredDay())){
                if (Duration.between(currentDate, hr.getExpiredDay()).toDays() == 1L){
                    ApplicationLogServiceImpl.sendEmail(hr.getEmail(), "CvBuilder subscription is going to expired!"
                            , "Dear user " + hr.getName() + ", your subscription is going to expired on " + hr.getExpiredDay()
                                    + ". CvBuilder apologise for this inconvenience.");
                } else if (Duration.between(currentDate, hr.getExpiredDay()).toDays() == 0L){
                    ApplicationLogServiceImpl.sendEmail(hr.getEmail(), "CvBuilder subscription is going to expired!"
                            , "Dear user " + hr.getName() + ", your subscription is expired! Thanks for your using.");
                }
            }
        }
    }
}
