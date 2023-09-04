package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.EducationDto;
import com.example.capstoneproject.Dto.InvolvementDto;
import com.example.capstoneproject.entity.Education;
import com.example.capstoneproject.entity.Involvement;
import com.example.capstoneproject.enums.CvStatus;
import com.example.capstoneproject.mapper.EducationMapper;
import com.example.capstoneproject.mapper.InvolvementMapper;
import com.example.capstoneproject.repository.EducationRepository;
import com.example.capstoneproject.repository.InvolvementRepository;
import com.example.capstoneproject.service.EducationService;
import com.example.capstoneproject.service.InvolvementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class InvolvementServiceImpl extends AbstractBaseService<Involvement, InvolvementDto, Integer> implements InvolvementService {
    @Autowired
    InvolvementRepository involvementRepository;

    @Autowired
    InvolvementMapper involvementMapper;

    public InvolvementServiceImpl(InvolvementRepository involvementRepository, InvolvementMapper involvementMapper) {
        super(involvementRepository, involvementMapper, involvementRepository::findById);
        this.involvementRepository = involvementRepository;
        this.involvementMapper = involvementMapper;
    }

    @Override
    public List<InvolvementDto> getAll() {
        List<Involvement> involvements = involvementRepository.findInvolvementsByStatus(CvStatus.ACTIVE);
        return involvementMapper.mapEntitiesToDtoes(involvements);
    }

    @Override
    public InvolvementDto create(InvolvementDto dto) {
        Involvement involvement = involvementMapper.mapDtoToEntity(dto);
        involvement.setStatus(CvStatus.ACTIVE);
        Involvement saved = involvementRepository.save(involvement);
        return involvementMapper.mapEntityToDto(saved);
    }

    @Override
    public InvolvementDto update(Integer id, InvolvementDto dto) {
        Optional<Involvement> existingInvolvementOptional = involvementRepository.findById(id);
        if (existingInvolvementOptional.isPresent()) {
            Involvement existingInvolvement = existingInvolvementOptional.get();
            if (dto.getOrganizationRole() != null && !existingInvolvement.getOrganizationRole().equals(dto.getOrganizationRole())) {
                existingInvolvement.setOrganizationRole(dto.getOrganizationRole());
            } else {
                throw new IllegalArgumentException("New Organization Role is the same as the existing involvement");
            }
            if (dto.getOrganizationName() != null && !existingInvolvement.getOrganizationName().equals(dto.getOrganizationName())) {
                existingInvolvement.setOrganizationName(dto.getOrganizationName());
            } else {
                throw new IllegalArgumentException("New Organization Name is the same as the existing involvement");
            }
            if (dto.getStartDate() != null && !existingInvolvement.getStartDate().equals(dto.getStartDate())) {
                existingInvolvement.setStartDate(dto.getStartDate());
            } else {
                throw new IllegalArgumentException("New Start Date is the same as the existing involvement");
            }
            if (dto.getEndDate() != null && !existingInvolvement.getEndDate().equals(dto.getEndDate())) {
                existingInvolvement.setEndDate(dto.getEndDate());
            } else {
                throw new IllegalArgumentException("New End Date is the same as the existing involvement");
            }
            if (dto.getCollegeLocation() != null && !existingInvolvement.getCollegeLocation().equals(dto.getCollegeLocation())) {
                existingInvolvement.setCollegeLocation(dto.getCollegeLocation());
            } else {
                throw new IllegalArgumentException("New College Location is the same as the existing involvement");
            }
            if (dto.getDescription() != null && !existingInvolvement.getDescription().equals(dto.getDescription())) {
                existingInvolvement.setDescription(dto.getDescription());
            } else {
                throw new IllegalArgumentException("New Description is the same as the existing involvement");
            }

            existingInvolvement.setStatus(CvStatus.ACTIVE);
            Involvement updated = involvementRepository.save(existingInvolvement);
            return involvementMapper.mapEntityToDto(updated);
        } else {
            throw new IllegalArgumentException("Involvement ID not found");
        }
    }

    @Override
    public void deleteById(Integer id) {
        Optional<Involvement> Optional = involvementRepository.findById(id);
        if (Optional.isPresent()) {
            Involvement involvement = Optional.get();
            involvement.setStatus(CvStatus.DELETED);
            involvementRepository.save(involvement);
        }
    }

}
