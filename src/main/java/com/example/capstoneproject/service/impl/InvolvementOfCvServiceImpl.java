package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.InvolvementOfCvDto;
import com.example.capstoneproject.Dto.InvolvementViewDto;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.CvStatus;
import com.example.capstoneproject.mapper.InvolvementOfCvMapper;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.InvolvementOfCvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvolvementOfCvServiceImpl extends AbstractBaseService<InvolvementOfCv, InvolvementOfCvDto, Integer> implements InvolvementOfCvService {
    @Autowired
    InvolvementOfCvRepository involvementOfCvRepository;

    @Autowired
    InvolvementOfCvMapper involvementOfCvMapper;

    @Autowired
    CvRepository cvRepository;

    @Autowired
    InvolvementRepository involvementRepository;

    public InvolvementOfCvServiceImpl(InvolvementOfCvRepository involvementOfCvRepository, InvolvementOfCvMapper involvementOfCvMapper) {
        super(involvementOfCvRepository, involvementOfCvMapper, involvementOfCvRepository::findById);
        this.involvementOfCvRepository = involvementOfCvRepository;
        this.involvementOfCvMapper = involvementOfCvMapper;
    }

    @Override
    public boolean createInvolvementOfCv(Integer cvId, Integer involvementId) {
        Involvement involvement = involvementRepository.findInvolvementById(involvementId, CvStatus.ACTIVE);
        Cv cv = cvRepository.findCvById(cvId,CvStatus.ACTIVE);
        if (involvement != null && cv != null) {
            InvolvementOfCv involvementOfCv = new InvolvementOfCv();
            involvementOfCv.setInvolvement(involvement);
            involvementOfCv.setCv(cv);
            involvementOfCvRepository.save(involvementOfCv);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteInvolvementOfCv(Integer cvId, Integer involvementId) {
        InvolvementOfCv involvementOfCv = involvementOfCvRepository.findByInvolvement_IdAndCv_Id(involvementId, cvId);

        if (involvementOfCv != null) {
            involvementOfCvRepository.delete(involvementOfCv);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<InvolvementViewDto> getActiveInvolvementsByCvId(Integer cvId) {
        List<InvolvementOfCv> involvementOfCvs = involvementOfCvRepository.findActiveInvolvementsByCvId(cvId, CvStatus.ACTIVE);
        List<InvolvementViewDto> involvementViewDtos = new ArrayList<>();

        for (InvolvementOfCv involvementOfCv : involvementOfCvs) {
            Involvement involvement = involvementOfCv.getInvolvement();
            InvolvementViewDto involvementViewDto = new InvolvementViewDto();
            involvementViewDto.setId(involvement.getId());
            involvementViewDto.setOrganizationRole(involvement.getOrganizationRole());
            involvementViewDto.setOrganizationName(involvement.getOrganizationName());
            involvementViewDto.setStartDate(involvement.getStartDate());
            involvementViewDto.setEndDate(involvement.getEndDate());
            involvementViewDto.setCollege(involvement.getCollege());
            involvementViewDto.setDescription(involvement.getDescription());
            involvementViewDtos.add(involvementViewDto);
        }

        return involvementViewDtos;
    }

    @Override
    public List<InvolvementViewDto> getAllInvolvement(int cvId) {
        List<Involvement> existingInvolvements = involvementOfCvRepository.findInvolvementsByCvId(cvId);
        List<Involvement> activeInvolvements = involvementRepository.findByStatus(CvStatus.ACTIVE);
        List<Involvement> unassignedInvolvements = activeInvolvements.stream()
                .filter(involvement -> !existingInvolvements.contains(involvement))
                .collect(Collectors.toList());

        return unassignedInvolvements.stream()
                .map(involvement -> {
                    InvolvementViewDto involvementViewDto = new InvolvementViewDto();
                    involvementViewDto.setId(involvement.getId());
                    involvementViewDto.setOrganizationRole(involvement.getOrganizationRole());
                    involvementViewDto.setOrganizationName(involvement.getOrganizationName());
                    involvementViewDto.setStartDate(involvement.getStartDate());
                    involvementViewDto.setEndDate(involvement.getEndDate());
                    involvementViewDto.setCollege(involvement.getCollege());
                    involvementViewDto.setDescription(involvement.getDescription());
                    return involvementViewDto;
                })
                .collect(Collectors.toList());
    }
}
