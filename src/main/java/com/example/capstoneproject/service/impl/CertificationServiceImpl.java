package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.CertificationDto;
import com.example.capstoneproject.Dto.CvBodyDto;
import com.example.capstoneproject.Dto.responses.CertificationViewDto;
import com.example.capstoneproject.entity.Certification;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.exception.ResourceNotFoundException;
import com.example.capstoneproject.mapper.CertificationMapper;
import com.example.capstoneproject.repository.CertificationRepository;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.service.CertificationService;
import com.example.capstoneproject.service.CvService;
import com.example.capstoneproject.service.UsersService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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


    @Autowired
    CvService cvService;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CvRepository cvRepository;

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
        Cv entity = cvRepository.getById(id);
        certification.setCv(entity);
        certification.setStatus(BasicStatus.ACTIVE);
        Certification saved = certificationRepository.save(certification);
        return certificationMapper.mapEntityToDto(saved);
    }

    @Override
    public boolean updateCertification(int UsersId, int certificationId, CertificationDto dto) {
        Optional<Certification> existingCertificationOptional = certificationRepository.findById(certificationId);
        if (existingCertificationOptional.isPresent()) {
            Certification existingCertification = existingCertificationOptional.get();
            if (existingCertification.getCv().getId() != UsersId) {
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
            if (dto.getEndYear() > 1950 && existingCertification.getEndYear() != dto.getEndYear()) {
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
    public void deleteCertificationById(Integer UsersId, Integer certificationId) {
        boolean isCertificationBelongsToCv = certificationRepository.existsByIdAndCv_Id(certificationId, UsersId);

        if (isCertificationBelongsToCv) {
            Optional<Certification> Optional = certificationRepository.findById(certificationId);
            if (Optional.isPresent()) {
                Certification certification = Optional.get();
                certification.setStatus(BasicStatus.DELETED);
                certificationRepository.save(certification);
            }
        } else {
            throw new IllegalArgumentException("certification with ID " + certificationId + " does not belong to Users with ID " + UsersId);
        }
    }

    @Override
    public CertificationDto getAndIsDisplay(int cvId, int id) throws JsonProcessingException {
        Certification certification = certificationRepository.getById(id);
        if (Objects.nonNull(certification)) {
            Cv cv = cvService.getCvById(cvId);
            CvBodyDto cvBodyDto = cv.deserialize();
            Optional<CertificationDto> dto = cvBodyDto.getCertifications().stream().filter(x -> x.getId() == id).findFirst();
            if (dto.isPresent()) {
                modelMapper.map(certification, dto.get());
                return dto.get();
            } else {
                throw new ResourceNotFoundException("Not found that id in cvBody");
            }
        } else {
            throw new ResourceNotFoundException("Not found that id in cvBody");
        }
    }

    @Override
    public CertificationDto getByIdInCvBody(int cvId, int id) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        Optional<CertificationDto> dto = cvBodyDto.getCertifications().stream().filter(x -> x.getId() == id).findFirst();
        if (dto.isPresent()) {
            return dto.get();
        } else {
            throw new ResourceNotFoundException("Not found that id in cvBody");
        }
    }

    @Override
    public List<CertificationDto> getAllARelationInCvBody(int cvId) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        List<CertificationDto> set = new ArrayList<>();
        cvBodyDto.getCertifications().stream().forEach(
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
    public boolean updateInCvBody(int cvId, int id, CertificationDto dto) throws JsonProcessingException {
        Cv cv = cvService.getCvById(cvId);
        CvBodyDto cvBodyDto = cv.deserialize();
        Optional<CertificationDto> relationDto = cvBodyDto.getCertifications().stream().filter(x -> x.getId() == id).findFirst();
        if (relationDto.isPresent()) {
            Certification certification = certificationRepository.getById(id);
            modelMapper.map(dto, certification);
            certificationRepository.save(certification);
            CertificationDto CertificationDto = relationDto.get();
            CertificationDto.setIsDisplay(dto.getIsDisplay());
            CertificationDto.setTheOrder(dto.getTheOrder());
            cvService.updateCvBody(cvId, cvBodyDto);
            return true;
        } else {
            throw new IllegalArgumentException("Certification ID not found in cvBody");
        }
    }


    @Override
    public CertificationDto createOfUserInCvBody(int cvId, CertificationDto dto) throws JsonProcessingException {
        Certification certification = certificationMapper.mapDtoToEntity(dto);
        Cv cv = cvRepository.findCvById(cvId, BasicStatus.ACTIVE);
        certification.setCv(cv);
        certification.setStatus(BasicStatus.ACTIVE);
        Certification saved = certificationRepository.save(certification);
        CertificationDto certificationDto = new CertificationDto();
        certificationDto.setId(saved.getId());
        List<Cv> list = cvRepository.findAllByUsersIdAndStatus(cv.getUser().getId(), BasicStatus.ACTIVE);
        list.stream().forEach(x -> {
            if (x.getId().equals(cvId)) {
                certificationDto.setIsDisplay(true);
            } else {
                certificationDto.setIsDisplay(false);
            }
            try {
                CvBodyDto cvBodyDto = x.deserialize();
                certificationDto.setTheOrder(cvBodyDto.getCertifications().size() + 1);
                cvBodyDto.getCertifications().add(certificationDto);
                cvService.updateCvBody(x.getId(), cvBodyDto);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
        return certificationMapper.mapEntityToDto(saved);
    }

    @Override
    public void deleteInCvBody(Integer cvId, Integer CertificationId) throws JsonProcessingException {

        Optional<Certification> Optional = certificationRepository.findById(CertificationId);
        if (Optional.isPresent()) {
            Certification certification = Optional.get();
            certification.setStatus(BasicStatus.DELETED);
            certificationRepository.delete(certification);
            try {
                CvBodyDto cvBodyDto = cvService.getCvBody(cvId);
                CertificationDto dto = cvBodyDto.getCertifications().stream().filter(e-> e.getId().equals(CertificationId)).findFirst().get();
                cvBodyDto.getCertifications().forEach(c -> {
                    if (Objects.nonNull(c.getTheOrder()) && c.getTheOrder() > dto.getTheOrder()){
                        c.setTheOrder(c.getTheOrder() - 1);
                    }
                });
                cvBodyDto.getCertifications().removeIf(e -> e.getId() == CertificationId);
                cvService.updateCvBody(cvId, cvBodyDto);

            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
