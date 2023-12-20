package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.HRDto;
import com.example.capstoneproject.Dto.NoteDto;
import com.example.capstoneproject.Dto.request.HRBankRequest;
import com.example.capstoneproject.Dto.responses.HRResponse;
import com.example.capstoneproject.Dto.responses.TransactionResponse;
import com.example.capstoneproject.entity.HR;
import com.example.capstoneproject.repository.HRRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface HRService {

    Boolean checkVip(Integer id);

    HRDto get(Integer id);

    HRDto update(HRResponse dto);

    HR create(HR dto);

    String register(TransactionResponse transactionDto) throws Exception;
}
