package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.CertificationDto;
import com.example.capstoneproject.entity.Certification;
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
    public List<CertificationDto> getAll() {
        List<Certification> certifications = certificationRepository.findCertificationsByStatus(CvStatus.ACTIVE);
        return certificationMapper.mapEntitiesToDtoes(certifications);
    }

    @Override
    public CertificationDto create(CertificationDto dto) {
        Certification certification = certificationMapper.mapDtoToEntity(dto);
        certification.setStatus(CvStatus.ACTIVE);
        Certification savedCertification = certificationRepository.save(certification);
        return certificationMapper.mapEntityToDto(savedCertification);
    }

    @Override
    public CertificationDto update(Integer id, CertificationDto dto) {
        Optional<Certification> existingCertificationOptional = certificationRepository.findById(id);
        if (existingCertificationOptional.isPresent()) {
            Certification existingCertification = existingCertificationOptional.get();
            if (dto.getTitle() != null && !existingCertification.getTitle().equals(dto.getTitle())) {
                existingCertification.setTitle(dto.getTitle());
            } else {
                throw new IllegalArgumentException("New certification title is the same as the existing certification");
            }
            if (dto.getCertificateSource() != null && !existingCertification.getCertificateSource().equals(dto.getCertificateSource())) {
                existingCertification.setCertificateSource(dto.getCertificateSource());
            } else {
                throw new IllegalArgumentException("New certification source is the same as the existing certification");
            }
            if (dto.getEndDate() != null && !existingCertification.getEndDate().equals(dto.getEndDate())) {
                existingCertification.setEndDate(dto.getEndDate());
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
            return certificationMapper.mapEntityToDto(updated);
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
