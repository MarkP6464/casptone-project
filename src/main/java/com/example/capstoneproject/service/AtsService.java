package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.AtsDto;
import com.example.capstoneproject.entity.Ats;
import org.springframework.stereotype.Service;

@Service
public interface AtsService extends BaseService<AtsDto, Integer> {
    Ats createAts(Ats ats);
    boolean deleteAts(Integer atsId);
}
