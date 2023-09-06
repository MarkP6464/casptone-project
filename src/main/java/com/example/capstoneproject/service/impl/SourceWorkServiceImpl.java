package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.SkillDto;
import com.example.capstoneproject.Dto.SkillViewDto;
import com.example.capstoneproject.Dto.SourceWorkDto;
import com.example.capstoneproject.Dto.SourceWorkViewDto;
import com.example.capstoneproject.entity.Skill;
import com.example.capstoneproject.entity.SourceWork;
import com.example.capstoneproject.enums.CvStatus;
import com.example.capstoneproject.mapper.SkillMapper;
import com.example.capstoneproject.mapper.SourceWorkMapper;
import com.example.capstoneproject.repository.SkillRepository;
import com.example.capstoneproject.repository.SourceWorkRepository;
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

    public SourceWorkServiceImpl(SourceWorkRepository sourceWorkRepository, SourceWorkMapper sourceWorkMapper) {
        super(sourceWorkRepository, sourceWorkMapper, sourceWorkRepository::findById);
        this.sourceWorkRepository = sourceWorkRepository;
        this.sourceWorkMapper = sourceWorkMapper;
    }

    @Override
    public boolean updateSourceWork(Integer id, SourceWorkViewDto dto) {
        Optional<SourceWork> existingSourceWorkOptional = sourceWorkRepository.findById(id);
        if (existingSourceWorkOptional.isPresent()) {
            SourceWork existingSourceWork = existingSourceWorkOptional.get();
            if (dto.getName() != null && !existingSourceWork.getName().equals(dto.getName())) {
                existingSourceWork.setName(dto.getName());
            } else {
                throw new IllegalArgumentException("New Name is the same as the existing skill");
            }
            if (dto.getCourseLocation() != null && !existingSourceWork.getCourseLocation().equals(dto.getCourseLocation())) {
                existingSourceWork.setCourseLocation(dto.getCourseLocation());
            } else {
                throw new IllegalArgumentException("New Course Location is the same as the existing source work");
            }
            if (dto.getEndDate() != null && !existingSourceWork.getEndDate().equals(dto.getEndDate())) {
                existingSourceWork.setEndDate(dto.getEndDate());
            } else {
                throw new IllegalArgumentException("New End Date is the same as the existing source work");
            }
            if (dto.getSkill() != null && !existingSourceWork.getSkill().equals(dto.getSkill())) {
                existingSourceWork.setSkill(dto.getSkill());
            } else {
                throw new IllegalArgumentException("New Skill is the same as the existing source work");
            }
            if (dto.getApplied() != null && !existingSourceWork.getApplied().equals(dto.getApplied())) {
                existingSourceWork.setApplied(dto.getApplied());
            } else {
                throw new IllegalArgumentException("New Applied is the same as the existing source work");
            }
            existingSourceWork.setStatus(CvStatus.ACTIVE);
            SourceWork updated = sourceWorkRepository.save(existingSourceWork);
            return true;
        } else {
            throw new IllegalArgumentException("Source Work ID not found");
        }
    }

    @Override
    public List<SourceWorkViewDto> getAllSourceWork(int cvId) {
        List<SourceWork> sourceWorks = sourceWorkRepository.findSourceWorksByStatus(cvId, CvStatus.ACTIVE);
        return sourceWorks.stream()
                .filter(sourceWork -> sourceWork.getStatus() == CvStatus.ACTIVE)
                .map(sourceWork -> {
                    SourceWorkViewDto sourceWorkViewDto = new SourceWorkViewDto();
                    sourceWorkViewDto.setId(sourceWork.getId());
                    sourceWorkViewDto.setName(sourceWork.getName());
                    sourceWorkViewDto.setCourseLocation(sourceWork.getCourseLocation());
                    sourceWorkViewDto.setEndDate(sourceWork.getEndDate());
                    sourceWorkViewDto.setSkill(sourceWork.getSkill());
                    sourceWorkViewDto.setApplied(sourceWork.getApplied());
                    sourceWorkViewDto.setStatus(sourceWork.getStatus());
                    return sourceWorkViewDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Integer id) {
        Optional<SourceWork> Optional = sourceWorkRepository.findById(id);
        if (Optional.isPresent()) {
            SourceWork sourceWork = Optional.get();
            sourceWork.setStatus(CvStatus.DELETED);
            sourceWorkRepository.save(sourceWork);
        }
    }

}
