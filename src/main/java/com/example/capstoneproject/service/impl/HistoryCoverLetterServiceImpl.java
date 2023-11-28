package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.HRDto;
import com.example.capstoneproject.Dto.HistoryCoverLetterDto;
import com.example.capstoneproject.Dto.TransactionDto;
import com.example.capstoneproject.Dto.responses.AdminConfigurationResponse;
import com.example.capstoneproject.Dto.responses.HRResponse;
import com.example.capstoneproject.Dto.responses.TransactionResponse;
import com.example.capstoneproject.entity.HR;
import com.example.capstoneproject.entity.HistoryCoverLetter;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.MoneyType;
import com.example.capstoneproject.enums.TransactionType;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.exception.ForbiddenException;
import com.example.capstoneproject.mapper.HRMapper;
import com.example.capstoneproject.repository.HRRepository;
import com.example.capstoneproject.repository.HistoryCoverLetterRepository;
import com.example.capstoneproject.repository.UsersRepository;
import com.example.capstoneproject.service.AdminConfigurationService;
import com.example.capstoneproject.service.HRService;
import com.example.capstoneproject.service.HistoryCoverLetterService;
import com.example.capstoneproject.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class HistoryCoverLetterServiceImpl implements HistoryCoverLetterService {
    @Autowired
    HistoryCoverLetterRepository historyCoverLetterRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public HistoryCoverLetterDto get(Integer id){
         Optional<HistoryCoverLetter> coverLetter = historyCoverLetterRepository.findById(id);
         if (coverLetter.isPresent()){
             HistoryCoverLetter historyCoverLetter = coverLetter.get();
             return modelMapper.map(historyCoverLetter, HistoryCoverLetterDto.class);
         } else throw new BadRequestException("not found History of the cover letter");
    }
}
