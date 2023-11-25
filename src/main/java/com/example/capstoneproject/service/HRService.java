package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.HRDto;
import com.example.capstoneproject.Dto.NoteDto;
import com.example.capstoneproject.repository.HRRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

public interface HRService {

    HRDto get(Integer id);

    HRDto update(HRDto dto);

    HRDto register() throws Exception;
}
