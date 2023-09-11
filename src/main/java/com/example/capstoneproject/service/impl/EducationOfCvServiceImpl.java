package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.EducationOfCvDto;
import com.example.capstoneproject.Dto.EducationViewDto;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.CvStatus;
import com.example.capstoneproject.mapper.EducationOfCvMapper;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.EducationOfCvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class EducationOfCvServiceImpl extends AbstractBaseService<EducationOfCv, EducationOfCvDto, Integer> implements EducationOfCvService {
    @Autowired
    EducationOfCvRepository educationOfCvRepository;

    @Autowired
    EducationOfCvMapper educationOfCvMapper;

    @Autowired
    CvRepository cvRepository;

    @Autowired
    EducationRepository educationRepository;

    public EducationOfCvServiceImpl(EducationOfCvRepository educationOfCvRepository, EducationOfCvMapper educationOfCvMapper) {
        super(educationOfCvRepository, educationOfCvMapper, educationOfCvRepository::findById);
        this.educationOfCvRepository = educationOfCvRepository;
        this.educationOfCvMapper = educationOfCvMapper;
    }

    @Override
    public boolean createEducationOfCv(Integer cvId, Integer educationId) {
        Education education = educationRepository.findEducationById(educationId, CvStatus.ACTIVE);
        Cv cv = cvRepository.findCvById(cvId,CvStatus.ACTIVE);
        if (education != null && cv != null) {
            EducationOfCv educationOfCv = new EducationOfCv();
            educationOfCv.setEducation(education);
            educationOfCv.setCv(cv);
            educationOfCvRepository.save(educationOfCv);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteEducationOfCv(Integer cvId, Integer educationId) {
        EducationOfCv educationOfCv = educationOfCvRepository.findByEducation_IdAndCv_Id(educationId, cvId);

        if (educationOfCv != null) {
            educationOfCvRepository.delete(educationOfCv);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<EducationViewDto> getActiveEducationsByCvId(Integer cvId) {
        List<EducationOfCv> educationOfCvs = educationOfCvRepository.findActiveEducationsByCvId(cvId, CvStatus.ACTIVE);
        List<EducationViewDto> educationViewDtos = new ArrayList<>();

        for (EducationOfCv educationOfCv : educationOfCvs) {
            Education education = educationOfCv.getEducation();
            EducationViewDto educationViewDto = new EducationViewDto();
            educationViewDto.setId(education.getId());
            educationViewDto.setDegree(education.getDegree());
            educationViewDto.setCollegeName(education.getCollegeName());
            educationViewDto.setLocation(education.getLocation());
            educationViewDto.setEndYear(education.getEndYear());
            educationViewDto.setMinor(education.getMinor());
            educationViewDto.setGpa(education.getGpa());
            educationViewDto.setDescription(education.getDescription());
            educationViewDtos.add(educationViewDto);
        }

        return educationViewDtos;
    }

    @Override
    public List<EducationViewDto> getAllEducation(int cvId) {
        List<Education> existingEducations = educationOfCvRepository.findEducationsByCvId(cvId);
        List<Education> activeEducations = educationRepository.findByStatus(CvStatus.ACTIVE);
        List<Education> unassignedEducations = activeEducations.stream()
                .filter(education -> !existingEducations.contains(education))
                .collect(Collectors.toList());

        return unassignedEducations.stream()
                .map(education -> {
                    EducationViewDto educationViewDto = new EducationViewDto();
                    educationViewDto.setId(education.getId());
                    educationViewDto.setDegree(education.getDegree());
                    educationViewDto.setCollegeName(education.getCollegeName());
                    educationViewDto.setLocation(education.getLocation());
                    educationViewDto.setEndYear(education.getEndYear());
                    educationViewDto.setMinor(education.getMinor());
                    educationViewDto.setGpa(education.getGpa());
                    educationViewDto.setDescription(education.getDescription());
                    return educationViewDto;
                })
                .collect(Collectors.toList());
    }
}
