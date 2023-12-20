package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.HRDto;
import com.example.capstoneproject.Dto.HistoryCoverLetterDto;
import com.example.capstoneproject.Dto.TransactionDto;
import com.example.capstoneproject.Dto.responses.AdminConfigurationResponse;
import com.example.capstoneproject.Dto.responses.HRResponse;
import com.example.capstoneproject.Dto.responses.TransactionResponse;
import com.example.capstoneproject.Dto.responses.UsersCvViewDto;
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

    @Autowired
    UsersRepository usersRepository;

    @Override
    public HistoryCoverLetterDto get(Integer id){
         Optional<HistoryCoverLetter> coverLetter = historyCoverLetterRepository.findById(id);
         if (coverLetter.isPresent()){
             HistoryCoverLetter historyCoverLetter = coverLetter.get();
             HistoryCoverLetterDto historyCoverLetterDto = new HistoryCoverLetterDto();
             historyCoverLetterDto.setId(historyCoverLetter.getId());
             historyCoverLetterDto.setTitle(historyCoverLetter.getTitle());
             historyCoverLetterDto.setDear(historyCoverLetter.getDear());
             historyCoverLetterDto.setDate(historyCoverLetter.getDate());
             historyCoverLetterDto.setCompany(historyCoverLetter.getCompany());
             historyCoverLetterDto.setDescription(historyCoverLetter.getDescription());
             Integer us = historyCoverLetter.getCoverLetter().getCv().getUser().getId();
             Optional<Users> usersOptional = usersRepository.findUsersById(historyCoverLetter.getCoverLetter().getCv().getUser().getId());
             if(usersOptional.isPresent()){
                 Users users = usersOptional.get();
                 UsersCvViewDto usersCvViewDto = new UsersCvViewDto();
                 usersCvViewDto.setFullName(users.getName());
                 usersCvViewDto.setEmail(users.getEmail());
                 usersCvViewDto.setPersonalWebsite(users.getPersonalWebsite());
                 usersCvViewDto.setPhone(users.getPhone());
                 usersCvViewDto.setLinkin(users.getLinkin());
                 usersCvViewDto.setCity(users.getCountry());
                 historyCoverLetterDto.setContact(usersCvViewDto);
             }
             return historyCoverLetterDto;
         } else throw new BadRequestException("not found History of the cover letter");
    }
}
