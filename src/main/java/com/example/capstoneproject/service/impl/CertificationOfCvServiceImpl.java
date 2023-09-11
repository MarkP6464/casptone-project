package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.CertificationOfCvDto;
import com.example.capstoneproject.Dto.CertificationViewDto;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.CvStatus;
import com.example.capstoneproject.mapper.CertificationOfCvMapper;
import com.example.capstoneproject.repository.*;
import com.example.capstoneproject.service.CertificationOfCvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class CertificationOfCvServiceImpl extends AbstractBaseService<CertificationOfCv, CertificationOfCvDto, Integer> implements CertificationOfCvService {

    @Autowired
    CertificationOfCvRepository certificationOfCvRepository;

    @Autowired
    CertificationOfCvMapper certificationOfCvMapper;

    @Autowired
    CvRepository cvRepository;

    @Autowired
    CertificationRepository certificationRepository;

    public CertificationOfCvServiceImpl(CertificationOfCvRepository certificationOfCvRepository, CertificationOfCvMapper certificationOfCvMapper) {
        super(certificationOfCvRepository, certificationOfCvMapper, certificationOfCvRepository::findById);
        this.certificationOfCvRepository = certificationOfCvRepository;
        this.certificationOfCvMapper = certificationOfCvMapper;
    }

    @Override
    public boolean createCertificationOfCv(Integer cvId, Integer certificationId) {
        Certification certification = certificationRepository.findCertificationById(certificationId, CvStatus.ACTIVE);
        Cv cv = cvRepository.findCvById(cvId,CvStatus.ACTIVE);
        if (certification != null && cv != null) {
            CertificationOfCv certificationOfCv = new CertificationOfCv();
            certificationOfCv.setCertification(certification);
            certificationOfCv.setCv(cv);
            certificationOfCvRepository.save(certificationOfCv);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteCertificationOfCv(Integer cvId, Integer certificationId) {
        CertificationOfCv certificationOfCv = certificationOfCvRepository.findByCertification_IdAndCv_Id(certificationId, cvId);

        if (certificationOfCv != null) {
            certificationOfCvRepository.delete(certificationOfCv);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<CertificationViewDto> getActiveCertificationsByCvId(Integer cvId) {
        List<CertificationOfCv> certificationOfCvs = certificationOfCvRepository.findActiveCertificationsByCvId(cvId, CvStatus.ACTIVE);
        List<CertificationViewDto> certificationViewDtos = new ArrayList<>();

        for (CertificationOfCv certificationOfCv : certificationOfCvs) {
            Certification certification = certificationOfCv.getCertification();
            CertificationViewDto certificationViewDto = new CertificationViewDto();
            certificationViewDto.setId(certification.getId());
            certificationViewDto.setName(certification.getName());
            certificationViewDto.setCertificateSource(certification.getCertificateSource());
            certificationViewDto.setEndYear(certification.getEndYear());
            certificationViewDto.setCertificateRelevance(certification.getCertificateRelevance());
            certificationViewDtos.add(certificationViewDto);
        }

        return certificationViewDtos;
    }

    @Override
    public List<CertificationViewDto> getAllCertification(int cvId) {
        List<Certification> existingCertifications = certificationOfCvRepository.findCertificationsByCvId(cvId);
        List<Certification> activeCertifications = certificationRepository.findByStatus(CvStatus.ACTIVE);
        List<Certification> unassignedCertifications = activeCertifications.stream()
                .filter(certification -> !existingCertifications.contains(certification))
                .collect(Collectors.toList());

        return unassignedCertifications.stream()
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
}
