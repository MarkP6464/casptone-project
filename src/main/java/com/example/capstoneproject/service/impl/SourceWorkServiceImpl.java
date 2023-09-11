package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.SkillDto;
import com.example.capstoneproject.Dto.SkillViewDto;
import com.example.capstoneproject.Dto.SourceWorkDto;
import com.example.capstoneproject.Dto.SourceWorkViewDto;
import com.example.capstoneproject.entity.Customer;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.Skill;
import com.example.capstoneproject.entity.SourceWork;
import com.example.capstoneproject.enums.CvStatus;
import com.example.capstoneproject.mapper.SkillMapper;
import com.example.capstoneproject.mapper.SourceWorkMapper;
import com.example.capstoneproject.repository.SkillRepository;
import com.example.capstoneproject.repository.SourceWorkRepository;
import com.example.capstoneproject.service.CustomerService;
import com.example.capstoneproject.service.CvService;
import com.example.capstoneproject.service.SkillService;
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
    CustomerService customerService;

    public SourceWorkServiceImpl(SourceWorkRepository sourceWorkRepository, SourceWorkMapper sourceWorkMapper) {
        super(sourceWorkRepository, sourceWorkMapper, sourceWorkRepository::findById);
        this.sourceWorkRepository = sourceWorkRepository;
        this.sourceWorkMapper = sourceWorkMapper;
    }

    @Override
    public SourceWorkDto createSourceWork(Integer id, SourceWorkDto dto) {
        SourceWork sourceWork = sourceWorkMapper.mapDtoToEntity(dto);
        Customer customer = customerService.getCustomerById(id);
        sourceWork.setCustomer(customer);
        sourceWork.setStatus(CvStatus.ACTIVE);
        SourceWork saved = sourceWorkRepository.save(sourceWork);
        return sourceWorkMapper.mapEntityToDto(saved);
    }

    @Override
    public boolean updateSourceWork(int customerId, int sourceWorkId, SourceWorkDto dto) {
        Optional<SourceWork> existingSourceWorkOptional = sourceWorkRepository.findById(sourceWorkId);
        if (existingSourceWorkOptional.isPresent()) {
            SourceWork existingSourceWork = existingSourceWorkOptional.get();

            if (existingSourceWork.getCustomer().getId() != customerId) {
                throw new IllegalArgumentException("Source Work does not belong to Customer with id " + customerId);
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
            existingSourceWork.setStatus(CvStatus.ACTIVE);
            sourceWorkRepository.save(existingSourceWork);
            return true;
        } else {
            throw new IllegalArgumentException("Source Work ID not found");
        }
    }


    @Override
    public List<SourceWorkViewDto> getAllSourceWork(int customerId) {
        List<SourceWork> sourceWorks = sourceWorkRepository.findSourceWorkByCv_IdAndStatus(customerId, CvStatus.ACTIVE);
        return sourceWorks.stream()
                .filter(sourceWork -> sourceWork.getStatus() == CvStatus.ACTIVE)
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
    public void deleteSourceWorkById(Integer customerId,Integer sourceId) {
        boolean isSourceWorkBelongsToCv = sourceWorkRepository.existsByIdAndCustomer_Id(sourceId, customerId);

        if (isSourceWorkBelongsToCv) {
            Optional<SourceWork> Optional = sourceWorkRepository.findById(sourceId);
            if (Optional.isPresent()) {
                SourceWork sourceWork = Optional.get();
                sourceWork.setStatus(CvStatus.DELETED);
                sourceWorkRepository.save(sourceWork);
            }
        } else {
            throw new IllegalArgumentException("Source Work with ID " + sourceId + " does not belong to Customer with ID " + customerId);
        }
    }

}
