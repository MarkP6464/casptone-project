package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.CvStatus;
import com.example.capstoneproject.mapper.EducationMapper;
import com.example.capstoneproject.mapper.InvolvementMapper;
import com.example.capstoneproject.repository.EducationRepository;
import com.example.capstoneproject.repository.InvolvementRepository;
import com.example.capstoneproject.service.CustomerService;
import com.example.capstoneproject.service.CvService;
import com.example.capstoneproject.service.EducationService;
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
    CustomerService customerService;

    public InvolvementServiceImpl(InvolvementRepository involvementRepository, InvolvementMapper involvementMapper) {
        super(involvementRepository, involvementMapper, involvementRepository::findById);
        this.involvementRepository = involvementRepository;
        this.involvementMapper = involvementMapper;
    }

    @Override
    public InvolvementDto createInvolvement(Integer id, InvolvementDto dto) {
        Involvement involvement = involvementMapper.mapDtoToEntity(dto);
        Customer customer = customerService.getCustomerById(id);
        involvement.setCustomer(customer);
        involvement.setStatus(CvStatus.ACTIVE);
        Involvement saved = involvementRepository.save(involvement);
        return involvementMapper.mapEntityToDto(saved);
    }

    @Override
    public boolean updateInvolvement(int customerId, int involvementId, InvolvementDto dto) {
        Optional<Involvement> existingInvolvementOptional = involvementRepository.findById(involvementId);
        if (existingInvolvementOptional.isPresent()) {
            Involvement existingInvolvement = existingInvolvementOptional.get();
            if (existingInvolvement.getCustomer().getId() != customerId) {
                throw new IllegalArgumentException("Involvement does not belong to Customer with id " + customerId);
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

            existingInvolvement.setStatus(CvStatus.ACTIVE);
            Involvement updated = involvementRepository.save(existingInvolvement);
            return true;
        } else {
            throw new IllegalArgumentException("Involvement ID not found");
        }
    }

    @Override
    public List<InvolvementViewDto> getAllInvolvement(int customerId) {
        List<Involvement> involvements = involvementRepository.findInvolvementsByStatus(customerId,CvStatus.ACTIVE);
        return involvements.stream()
                .filter(involvement -> involvement.getStatus() == CvStatus.ACTIVE)
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
    public void deleteInvolvementById(Integer customerId,Integer involvementId) {
        boolean isInvolvementBelongsToCv = involvementRepository.existsByIdAndCustomer_Id(involvementId, customerId);

        if (isInvolvementBelongsToCv) {
            Optional<Involvement> Optional = involvementRepository.findById(involvementId);
            if (Optional.isPresent()) {
                Involvement involvement = Optional.get();
                involvement.setStatus(CvStatus.DELETED);
                involvementRepository.save(involvement);
            }
        } else {
            throw new IllegalArgumentException("Project with ID " + involvementId + " does not belong to Customer with ID " + customerId);
        }
    }

}
