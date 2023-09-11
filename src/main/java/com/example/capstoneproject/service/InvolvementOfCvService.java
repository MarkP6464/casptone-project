package com.example.capstoneproject.service;

import com.example.capstoneproject.Dto.ExperienceOfCvDto;
import com.example.capstoneproject.Dto.ExperienceViewDto;
import com.example.capstoneproject.Dto.InvolvementOfCvDto;
import com.example.capstoneproject.Dto.InvolvementViewDto;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface InvolvementOfCvService extends BaseService<InvolvementOfCvDto, Integer> {
    boolean createInvolvementOfCv(Integer cvId, Integer involvementId);
    boolean deleteInvolvementOfCv(Integer cvId, Integer involvementId);
    List<InvolvementViewDto> getActiveInvolvementsByCvId(Integer cvId);
    List<InvolvementViewDto> getAllInvolvement(int cvId);
}
