package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.CvBodyDto;
import com.example.capstoneproject.Dto.InvolvementDto;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.Involvement;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.exception.ResourceNotFoundException;
import com.example.capstoneproject.mapper.InvolvementMapper;
import com.example.capstoneproject.repository.InvolvementRepository;
import com.example.capstoneproject.service.CvService;
import com.example.capstoneproject.service.InvolvementService;
import com.example.capstoneproject.service.UsersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvolvementServiceImpl extends AbstractBaseService<Involvement, InvolvementDto, Integer> implements InvolvementService {
    @Autowired
    InvolvementRepository involvementRepository;

    @Autowired
    InvolvementMapper involvementMapper;

    @Autowired
    CvService cvService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UsersService usersService;

    public InvolvementServiceImpl(InvolvementRepository involvementRepository, InvolvementMapper involvementMapper) {
        super(involvementRepository, involvementMapper, involvementRepository::findById);
        this.involvementRepository = involvementRepository;
        this.involvementMapper = involvementMapper;
    }

    @Override
    public InvolvementDto createInvolvement(Integer id, InvolvementDto dto) {
        Involvement involvement = involvementMapper.mapDtoToEntity(dto);
        Users Users = usersService.getUsersById(id);
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
    public List<InvolvementDto> getAllInvolvement(int UsersId) {
        List<Involvement> involvements = involvementRepository.findInvolvementsByStatus(UsersId, BasicStatus.ACTIVE);
        return involvements.stream()
                .filter(involvement -> involvement.getStatus() == BasicStatus.ACTIVE)
                .map(involvement -> {
                    InvolvementDto involvementDto = new InvolvementDto();
                    involvementDto.setId(involvement.getId());
                    involvementDto.setOrganizationRole(involvement.getOrganizationRole());
                    involvementDto.setOrganizationName(involvement.getOrganizationName());
                    involvementDto.setStartDate(involvement.getStartDate());
                    involvementDto.setEndDate(involvement.getEndDate());
                    involvementDto.setCollege(involvement.getCollege());
                    involvementDto.setDescription(involvement.getDescription());
                    return involvementDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteInvolvementById(Integer UsersId, Integer involvementId) {
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


    @Override
    public InvolvementDto getAndIsDisplay(int cvId, int id) throws JsonProcessingException {
        Involvement education = involvementRepository.getById(id);
        if (Objects.nonNull(education)) {
            Cv cv = cvService.getCvById(cvId);
            CvBodyDto cvBodyDto = cv.deserialize();
            Optional<InvolvementDto> dto = cvBodyDto.getInvolvements().stream().filter(x -> x.getId() == id).findFirst();
            if (dto.isPresent()) {
                modelMapper.map(education, dto.get());
                return dto.get();
            } else {
                throw new ResourceNotFoundException("Not found that id in cvBody");
            }
        } else {
            throw new ResourceNotFoundException("Not found that id in cvBody");
        }
    }

    @Override
    public InvolvementDto getByIdInCvBody(int cvId, int id) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        Optional<InvolvementDto> dto = cvBodyDto.getInvolvements().stream().filter(x -> x.getId() == id).findFirst();
        if (dto.isPresent()) {
            return dto.get();
        } else {
            throw new ResourceNotFoundException("Not found that id in cvBody");
        }
    }

    @Override
    public Set<InvolvementDto> getAllARelationInCvBody(int cvId) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        Set<InvolvementDto> set = new HashSet<>();
        cvBodyDto.getInvolvements().stream().forEach(
                e -> {
                    try {
                        set.add(getAndIsDisplay(cvId, e.getId()));
                    } catch (JsonProcessingException ex) {
                        throw new RuntimeException(ex);
                    }
                }
        );
        return set;
    }

    @Override
    public boolean updateInCvBody(int cvId, int id, InvolvementDto dto) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        Optional<InvolvementDto> relationDto = cvBodyDto.getInvolvements().stream().filter(x -> x.getId() == id).findFirst();
        if (relationDto.isPresent()) {
            Involvement education = involvementRepository.getById(id);
            modelMapper.map(dto, education);
            involvementRepository.save(education);
            InvolvementDto educationDto = relationDto.get();
            educationDto.setIsDisplay(dto.getIsDisplay());
            cvService.updateCvBody(cvId, cvBodyDto);
            return true;
        } else {
            throw new IllegalArgumentException("education ID not found in cvBody");
        }
    }


    @Override
    public InvolvementDto createOfUserInCvBody(int cvId, InvolvementDto dto) throws JsonProcessingException {
        Involvement education = involvementMapper.mapDtoToEntity(dto);
        Users Users = usersService.getUsersById(cvService.getCvById(cvId).getUser().getId());
        education.setUser(Users);
        education.setStatus(BasicStatus.ACTIVE);
        Involvement saved = involvementRepository.save(education);
        InvolvementDto involvementDto = new InvolvementDto();
        involvementDto.setId(saved.getId());
        CvBodyDto cvBodyDto = cvService.getCvBody(cvId);
        cvBodyDto.getInvolvements().add(involvementDto);
        involvementDto.setIsDisplay(true);
        cvService.updateCvBody(cvId, cvBodyDto);
        return involvementDto;
    }

    @Override
    public void deleteInCvBody(Integer cvId, Integer id) throws JsonProcessingException {

        Optional<Involvement> Optional = involvementRepository.findById(id);
        if (Optional.isPresent()) {
            Involvement education = Optional.get();
            education.setStatus(BasicStatus.DELETED);
            involvementRepository.save(education);
            CvBodyDto cvBodyDto = cvService.getCvBody(cvId);
            cvBodyDto.getEducations().removeIf(x -> x.getId() == id);
            cvService.updateCvBody(cvId, cvBodyDto);
        }
    }


}
