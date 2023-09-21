package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.mapper.InvolvementMapper;
import com.example.capstoneproject.repository.InvolvementRepository;
import com.example.capstoneproject.service.UsersService;
import com.example.capstoneproject.service.InvolvementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvolvementServiceImpl extends AbstractBaseService<Involvement, InvolvementDto, Integer> implements InvolvementService {
    @Autowired
    InvolvementRepository involvementRepository;

    @Autowired
    InvolvementMapper involvementMapper;

    @Autowired
    UsersService UsersService;

    public InvolvementServiceImpl(InvolvementRepository involvementRepository, InvolvementMapper involvementMapper) {
        super(involvementRepository, involvementMapper, involvementRepository::findById);
        this.involvementRepository = involvementRepository;
        this.involvementMapper = involvementMapper;
    }

    @Override
    public InvolvementDto createInvolvement(Integer id, InvolvementDto dto) {
        Involvement involvement = involvementMapper.mapDtoToEntity(dto);
        Users Users = UsersService.getUsersById(id);
        involvement.setUser(Users);
        involvement.setStatus(BasicStatus.ACTIVE);
        Involvement saved = involvementRepository.save(involvement);
        return involvementMapper.mapEntityToDto(saved);
    }

    @Override
    public boolean updateInvolvement(int UsersId, int involvementId, InvolvementDto dto) {
        Optional<Involvement> existingInvolvementOptional = involvementRepository.findById(involvementId);
        if (existingInvolvementOptional.isPresent()) {
            Involvement existingInvolvement = existingInvolvementOptional.get();
            if (existingInvolvement.getUser().getId() != UsersId) {
                throw new IllegalArgumentException("Involvement does not belong to Users with id " + UsersId);
            }
            if (dto.getOrganizationRole() != null && !existingInvolvement.getOrganizationRole().equals(dto.getOrganizationRole())) {
                existingInvolvement.setOrganizationRole(dto.getOrganizationRole());
            } else {
                existingInvolvement.setOrganizationRole(existingInvolvement.getOrganizationRole());
            }
            if (dto.getOrganizationName() != null && !existingInvolvement.getOrganizationName().equals(dto.getOrganizationName())) {
                existingInvolvement.setOrganizationName(dto.getOrganizationName());
            } else {
                existingInvolvement.setOrganizationName(existingInvolvement.getOrganizationName());
            }
            if (dto.getStartDate() != null && !existingInvolvement.getStartDate().equals(dto.getStartDate())) {
                existingInvolvement.setStartDate(dto.getStartDate());
            } else {
                existingInvolvement.setStartDate(existingInvolvement.getStartDate());
            }
            if (dto.getEndDate() != null && !existingInvolvement.getEndDate().equals(dto.getEndDate())) {
                existingInvolvement.setEndDate(dto.getEndDate());
            } else {
                existingInvolvement.setEndDate(existingInvolvement.getEndDate());
            }
            if (dto.getCollege() != null && !existingInvolvement.getCollege().equals(dto.getCollege())) {
                existingInvolvement.setCollege(dto.getCollege());
            } else {
                existingInvolvement.setCollege(existingInvolvement.getCollege());
            }
            if (dto.getDescription() != null && !existingInvolvement.getDescription().equals(dto.getDescription())) {
                existingInvolvement.setDescription(dto.getDescription());
            } else {
                existingInvolvement.setDescription(existingInvolvement.getDescription());
            }

            existingInvolvement.setStatus(BasicStatus.ACTIVE);
            Involvement updated = involvementRepository.save(existingInvolvement);
            return true;
        } else {
            throw new IllegalArgumentException("Involvement ID not found");
        }
    }

    @Override
    public List<InvolvementViewDto> getAllInvolvement(int UsersId) {
        List<Involvement> involvements = involvementRepository.findInvolvementsByStatus(UsersId, BasicStatus.ACTIVE);
        return involvements.stream()
                .filter(involvement -> involvement.getStatus() == BasicStatus.ACTIVE)
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

    @Override
    public void deleteInvolvementById(Integer UsersId,Integer involvementId) {
        boolean isInvolvementBelongsToCv = involvementRepository.existsByIdAndUser_Id(involvementId, UsersId);

        if (isInvolvementBelongsToCv) {
            Optional<Involvement> Optional = involvementRepository.findById(involvementId);
            if (Optional.isPresent()) {
                Involvement involvement = Optional.get();
                involvement.setStatus(BasicStatus.DELETED);
                involvementRepository.save(involvement);
            }
        } else {
            throw new IllegalArgumentException("Project with ID " + involvementId + " does not belong to Users with ID " + UsersId);
        }
    }

}
