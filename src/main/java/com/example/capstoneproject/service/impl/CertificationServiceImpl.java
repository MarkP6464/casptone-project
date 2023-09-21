package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.CertificationDto;
import com.example.capstoneproject.Dto.CertificationViewDto;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.mapper.CertificationMapper;
import com.example.capstoneproject.repository.CertificationRepository;
import com.example.capstoneproject.service.CertificationService;
import com.example.capstoneproject.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CertificationServiceImpl extends AbstractBaseService<Certification, CertificationDto, Integer> implements CertificationService {
    @Autowired
    CertificationRepository certificationRepository;

    @Autowired
    CertificationMapper certificationMapper;

    @Autowired
    UsersService usersService;

    public CertificationServiceImpl(CertificationRepository certificationRepository, CertificationMapper certificationMapper) {
        super(certificationRepository, certificationMapper, certificationRepository::findById);
        this.certificationRepository = certificationRepository;
        this.certificationMapper = certificationMapper;
    }

    @Override
    public List<CertificationViewDto> getAllCertification(int cvId) {
        List<Certification> certifications = certificationRepository.findCertificationsByStatus(cvId, BasicStatus.ACTIVE);
        return certifications.stream()
                .filter(certification -> certification.getStatus() == BasicStatus.ACTIVE)
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
        Users Users = usersService.getUsersById(id);
        certification.setUser(Users);
        certification.setStatus(BasicStatus.ACTIVE);
        Certification saved = certificationRepository.save(certification);
        return certificationMapper.mapEntityToDto(saved);
    }

    @Override
    public boolean updateCertification(int UsersId, int educationId, CertificationDto dto) {
        Optional<Certification> existingCertificationOptional = certificationRepository.findById(educationId);
        if (existingCertificationOptional.isPresent()) {
            Certification existingCertification = existingCertificationOptional.get();
            if (existingCertification.getUser().getId() != UsersId) {
                throw new IllegalArgumentException("Certification does not belong to Users with id " + UsersId);
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
            existingCertification.setStatus(BasicStatus.ACTIVE);
            Certification updated = certificationRepository.save(existingCertification);
            return true;
        } else {
            throw new IllegalArgumentException("Certification ID not found");
        }
    }

    @Override
    public void deleteCertificationById(Integer UsersId,Integer certificationId) {
        boolean isCertificationBelongsToCv = certificationRepository.existsByIdAndUser_Id(certificationId, UsersId);

        if (isCertificationBelongsToCv) {
            Optional<Certification> Optional = certificationRepository.findById(certificationId);
            if (Optional.isPresent()) {
                Certification certification = Optional.get();
                certification.setStatus(BasicStatus.DELETED);
                certificationRepository.save(certification);
            }
        } else {
            throw new IllegalArgumentException("Education with ID " + certificationId + " does not belong to Users with ID " + UsersId);
        }
    }

}
