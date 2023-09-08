package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.CertificationDto;
import com.example.capstoneproject.Dto.CertificationViewDto;
import com.example.capstoneproject.Dto.ProjectDto;
import com.example.capstoneproject.entity.Certification;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.Education;
import com.example.capstoneproject.entity.Project;
import com.example.capstoneproject.enums.CvStatus;
import com.example.capstoneproject.mapper.AbstractMapper;
import com.example.capstoneproject.mapper.CertificationMapper;
import com.example.capstoneproject.repository.CertificationRepository;
import com.example.capstoneproject.service.CertificationService;
import com.example.capstoneproject.service.CvService;
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

    @Autowired
    CvService cvService;

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
                    return certificationViewDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CertificationDto createCertification(Integer id, CertificationDto dto) {
        Certification certification = certificationMapper.mapDtoToEntity(dto);
        Cv cv = cvService.getCvById(id);
        certification.setCv(cv);
        certification.setStatus(CvStatus.ACTIVE);
        Certification saved = certificationRepository.save(certification);
        return certificationMapper.mapEntityToDto(saved);
    }

    @Override
    public boolean updateCertification(int cvId, int educationId, CertificationDto dto) {
        Optional<Certification> existingCertificationOptional = certificationRepository.findById(educationId);
        if (existingCertificationOptional.isPresent()) {
            Certification existingCertification = existingCertificationOptional.get();
            if (existingCertification.getCv().getId() != cvId) {
                throw new IllegalArgumentException("Certification does not belong to CV with id " + cvId);
            }
            if (dto.getName() != null && !existingCertification.getName().equals(dto.getName())) {
                existingCertification.setName(dto.getName());
            } else {
                existingCertification.setName(existingCertification.getName());
            }
            if (dto.getCertificateSource() != null && !existingCertification.getCertificateSource().equals(dto.getCertificateSource())) {
                existingCertification.setCertificateSource(dto.getCertificateSource());
            } else {
                existingCertification.setCertificateSource(existingCertification.getCertificateSource());
            }
            if (dto.getEndYear() >1950 && existingCertification.getEndYear()!=dto.getEndYear()) {
                existingCertification.setEndYear(dto.getEndYear());
            } else {
                existingCertification.setEndYear(existingCertification.getEndYear());
            }
            if (dto.getCertificateRelevance() != null && !existingCertification.getCertificateRelevance().equals(dto.getCertificateRelevance())) {
                existingCertification.setCertificateRelevance(dto.getCertificateRelevance());
            } else {
                existingCertification.setCertificateRelevance(existingCertification.getCertificateRelevance());
            }
            existingCertification.setStatus(CvStatus.ACTIVE);
            Certification updated = certificationRepository.save(existingCertification);
            return true;
        } else {
            throw new IllegalArgumentException("Certification ID not found");
        }
    }

    @Override
    public void deleteCertificationById(Integer cvId,Integer certificationId) {
        boolean isCertificationBelongsToCv = certificationRepository.existsByIdAndCv_Id(certificationId, cvId);

        if (isCertificationBelongsToCv) {
            Optional<Certification> Optional = certificationRepository.findById(certificationId);
            if (Optional.isPresent()) {
                Certification certification = Optional.get();
                certification.setStatus(CvStatus.DELETED);
                certificationRepository.save(certification);
            }
        } else {
            throw new IllegalArgumentException("Education with ID " + certificationId + " does not belong to CV with ID " + cvId);
        }
    }

}
