package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.SourceWorkDto;
import com.example.capstoneproject.Dto.SourceWorkViewDto;
import com.example.capstoneproject.entity.SourceWork;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.mapper.SourceWorkMapper;
import com.example.capstoneproject.repository.SourceWorkRepository;
import com.example.capstoneproject.service.UsersService;
import com.example.capstoneproject.service.SourceWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SourceWorkServiceImpl extends AbstractBaseService<SourceWork, SourceWorkDto, Integer> implements SourceWorkService {
    @Autowired
    SourceWorkRepository sourceWorkRepository;

    @Autowired
    SourceWorkMapper sourceWorkMapper;

    @Autowired
    UsersService UsersService;

    public SourceWorkServiceImpl(SourceWorkRepository sourceWorkRepository, SourceWorkMapper sourceWorkMapper) {
        super(sourceWorkRepository, sourceWorkMapper, sourceWorkRepository::findById);
        this.sourceWorkRepository = sourceWorkRepository;
        this.sourceWorkMapper = sourceWorkMapper;
    }

    @Override
    public SourceWorkDto createSourceWork(Integer id, SourceWorkDto dto) {
        SourceWork sourceWork = sourceWorkMapper.mapDtoToEntity(dto);
        Users Users = UsersService.getUsersById(id);
        sourceWork.setUser(Users);
        sourceWork.setStatus(BasicStatus.ACTIVE);
        SourceWork saved = sourceWorkRepository.save(sourceWork);
        return sourceWorkMapper.mapEntityToDto(saved);
    }

    @Override
    public boolean updateSourceWork(int UsersId, int sourceWorkId, SourceWorkDto dto) {
        Optional<SourceWork> existingSourceWorkOptional = sourceWorkRepository.findById(sourceWorkId);
        if (existingSourceWorkOptional.isPresent()) {
            SourceWork existingSourceWork = existingSourceWorkOptional.get();

            if (existingSourceWork.getUser().getId() != UsersId) {
                throw new IllegalArgumentException("Source Work does not belong to Users with id " + UsersId);
            }

            if (dto.getName() != null && !existingSourceWork.getName().equals(dto.getName())) {
                existingSourceWork.setName(dto.getName());
            } else {
                existingSourceWork.setName(existingSourceWork.getName());
            }
            if (dto.getCourseLocation() != null && !existingSourceWork.getCourseLocation().equals(dto.getCourseLocation())) {
                existingSourceWork.setCourseLocation(dto.getCourseLocation());
            } else {
                existingSourceWork.setCourseLocation(existingSourceWork.getCourseLocation());
            }
            if (dto.getEndYear() > 1950 && existingSourceWork.getEndYear() != dto.getEndYear()) {
                existingSourceWork.setEndYear(dto.getEndYear());
            } else {
                existingSourceWork.setEndYear(existingSourceWork.getEndYear());
            }
            if (dto.getSkill() != null && !existingSourceWork.getSkill().equals(dto.getSkill())) {
                existingSourceWork.setSkill(dto.getSkill());
            } else {
                existingSourceWork.setSkill(existingSourceWork.getSkill());
            }
            if (dto.getDescription() != null && !existingSourceWork.getDescription().equals(dto.getDescription())) {
                existingSourceWork.setDescription(dto.getDescription());
            } else {
                existingSourceWork.setDescription(existingSourceWork.getDescription());
            }
            existingSourceWork.setStatus(BasicStatus.ACTIVE);
            sourceWorkRepository.save(existingSourceWork);
            return true;
        } else {
            throw new IllegalArgumentException("Source Work ID not found");
        }
    }


    @Override
    public List<SourceWorkViewDto> getAllSourceWork(int UsersId) {
        List<SourceWork> sourceWorks = sourceWorkRepository.findSourceWorkByCv_IdAndStatus(UsersId, BasicStatus.ACTIVE);
        return sourceWorks.stream()
                .filter(sourceWork -> sourceWork.getStatus() == BasicStatus.ACTIVE)
                .map(sourceWork -> {
                    SourceWorkViewDto sourceWorkViewDto = new SourceWorkViewDto();
                    sourceWorkViewDto.setId(sourceWork.getId());
                    sourceWorkViewDto.setName(sourceWork.getName());
                    sourceWorkViewDto.setCourseLocation(sourceWork.getCourseLocation());
                    sourceWorkViewDto.setEndYear(sourceWork.getEndYear());
                    sourceWorkViewDto.setSkill(sourceWork.getSkill());
                    sourceWorkViewDto.setDescription(sourceWork.getDescription());
                    return sourceWorkViewDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteSourceWorkById(Integer UsersId,Integer sourceId) {
        boolean isSourceWorkBelongsToCv = sourceWorkRepository.existsByIdAndUser_Id(sourceId, UsersId);

        if (isSourceWorkBelongsToCv) {
            Optional<SourceWork> Optional = sourceWorkRepository.findById(sourceId);
            if (Optional.isPresent()) {
                SourceWork sourceWork = Optional.get();
                sourceWork.setStatus(BasicStatus.DELETED);
                sourceWorkRepository.save(sourceWork);
            }
        } else {
            throw new IllegalArgumentException("Source Work with ID " + sourceId + " does not belong to Users with ID " + UsersId);
        }
    }

}
