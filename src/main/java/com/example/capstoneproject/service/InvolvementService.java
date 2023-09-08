package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InvolvementService extends BaseService<InvolvementDto, Integer> {
    boolean updateInvolvement(int cvId, int involvementId, InvolvementDto dto);
    List<InvolvementViewDto> getAllInvolvement(int cvId);
    InvolvementDto createInvolvement(Integer id, InvolvementDto dto);
    void deleteInvolvementById(Integer cvId,Integer involvementId);
}
