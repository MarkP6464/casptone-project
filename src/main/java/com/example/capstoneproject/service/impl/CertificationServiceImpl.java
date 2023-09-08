package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.CertificationDto;
import com.example.capstoneproject.Dto.CertificationViewDto;
import com.example.capstoneproject.Dto.ProjectDto;
import com.example.capstoneproject.entity.Certification;
import com.example.capstoneproject.entity.Project;
import com.example.capstoneproject.enums.CvStatus;
import com.example.capstoneproject.mapper.AbstractMapper;
import com.example.capstoneproject.mapper.CertificationMapper;
import com.example.capstoneproject.repository.CertificationRepository;
import com.example.capstoneproject.service.CertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CertificationServiceImpl extends AbstractBaseService<Certification, CertificationDto, Integer> implements CertificationService {
    @Autowired
    CertificationRepository certificationRepository;

    @Autowired
    CertificationMapper certificationMapper;

    public CertificationServiceImpl(CertificationRepository certificationRepository, CertificationMapper certificationMapper) {
        super(certificationRepository, certificationMapper, certificationRepository::findById);
        this.certificationRepository = certificationRepository;
        this.certificationMapper = certificationMapper;
    }

    @Override
    public List<CertificationViewDto> getAllCertification(int cvId) {
        List<Certification> certifications = certificationRepository.findCertificationsByStatus(cvId,CvStatus.ACTIVE);
        return certifications.stream()
                .filter(certification -> certification.getStatus() == CvStatus.ACTIVE)
                .map(certification -> {
                    CertificationViewDto certificationViewDto = new CertificationViewDto();
                    certificationViewDto.setId(certification.getId());
                    certificationViewDto.setName(certification.getName());
                    certificationViewDto.setCertificateSource(certification.getCertificateSource());
                    certificationViewDto.setEndYear(certification.getEndYear());
                    certificationViewDto.setCertificateRelevance(certification.getCertificateRelevance());
                    certificationViewDto.setStatus(certification.getStatus());
                    return certificationViewDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CertificationDto create(CertificationDto dto) {
        Certification certification = certificationMapper.mapDtoToEntity(dto);
        certification.setStatus(CvStatus.ACTIVE);
        Certification saved = certificationRepository.save(certification);
        return certificationMapper.mapEntityToDto(saved);
    }

    @Override
    public boolean updateCertification(Integer id, CertificationViewDto dto) {
        Optional<Certification> existingCertificationOptional = certificationRepository.findById(id);
        if (existingCertificationOptional.isPresent()) {
            Certification existingCertification = existingCertificationOptional.get();
            if (dto.getName() != null && !existingCertification.getName().equals(dto.getName())) {
                existingCertification.setName(dto.getName());
            } else {
                throw new IllegalArgumentException("New certification title is the same as the existing certification");
            }
            if (dto.getCertificateSource() != null && !existingCertification.getCertificateSource().equals(dto.getCertificateSource())) {
                existingCertification.setCertificateSource(dto.getCertificateSource());
            } else {
                throw new IllegalArgumentException("New certification source is the same as the existing certification");
            }
            if (dto.getEndYear() >1950 && existingCertification.getEndYear()!=dto.getEndYear()) {
                existingCertification.setEndYear(dto.getEndYear());
            } else {
                throw new IllegalArgumentException("New certification end date is the same as the existing certification");
            }
            if (dto.getCertificateRelevance() != null && !existingCertification.getCertificateRelevance().equals(dto.getCertificateRelevance())) {
                existingCertification.setCertificateRelevance(dto.getCertificateRelevance());
            } else {
                throw new IllegalArgumentException("New certification relevance is the same as the existing certification");
            }
            existingCertification.setStatus(CvStatus.ACTIVE);
            Certification updated = certificationRepository.save(existingCertification);
            return true;
        } else {
            throw new IllegalArgumentException("Certification ID not found");
        }
    }

    @Override
    public void deleteById(Integer id) {
        Optional<Certification> Optional = certificationRepository.findById(id);
        if (Optional.isPresent()) {
            Certification certification = Optional.get();
            certification.setStatus(CvStatus.DELETED);
            certificationRepository.save(certification);
        }
    }

}
