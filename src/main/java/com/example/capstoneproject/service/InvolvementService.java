package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvolvementService extends BaseService<InvolvementDto, Integer> {
    boolean updateInvolvement(Integer id, InvolvementViewDto dto);
    List<InvolvementViewDto> getAllInvolvement(int cvId);
}
