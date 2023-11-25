package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.HRDto;
import com.example.capstoneproject.Dto.TransactionDto;
import com.example.capstoneproject.constant.PaymentConstant;
import com.example.capstoneproject.entity.HR;
import com.example.capstoneproject.entity.ReviewRequest;
import com.example.capstoneproject.entity.Transaction;
import com.example.capstoneproject.entity.Users;
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

import java.time.LocalDateTime;
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
    public HRDto register() throws Exception {
        Users users = (Users) SecurityUtil.getLoginUser();
        if (users instanceof HR){
            HR hr = (HR) users;
            TransactionDto transactionDto = new TransactionDto();
            transactionDto.setExpenditure(PaymentConstant.vipRatio);
            transactionDto.setTransactionType(TransactionType.ADD);
            transactionService.create(transactionDto);
        } else {
            throw new ForbiddenException("You are not HR!");
        }
        return null;
    }
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void checkSubscription(){
//        LocalDateTime currentDateTime = LocalDateTime.now();
//        List<ReviewRequest> reviewRequests = reviewRequestRepository.findAllByDeadline(currentDateTime);
//        for (ReviewRequest reviewRequest : reviewRequests) {
//            reviewRequest.setStatus(StatusReview.Overdue);
//            reviewRequestRepository.save(reviewRequest);
//            if(reviewRequestRepository.countByExpertIdAndStatus(reviewRequest.getExpertId(), StatusReview.Overdue)>=3){
//                expertService.punishExpert(reviewRequest.getExpertId());
//            }
//        }
//        expertService.unPunishExpert();
//    }
}
