package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.*;
import com.example.capstoneproject.entity.*;
import com.example.capstoneproject.enums.CvStatus;
import com.example.capstoneproject.mapper.CertificationMapper;
import com.example.capstoneproject.mapper.ContactMapper;
import com.example.capstoneproject.mapper.CvMapper;
import com.example.capstoneproject.mapper.EducationMapper;
import com.example.capstoneproject.repository.ContactRepository;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.service.ContactService;
import com.example.capstoneproject.service.CvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class CvServiceImpl extends AbstractBaseService<Cv, CvDto, Integer> implements CvService {
    @Autowired
    CvRepository cvRepository;

    @Autowired
    CvMapper cvMapper;

    @Autowired
    CertificationMapper certificationMapper;

    @Autowired
    EducationMapper educationMapper;

    public CvServiceImpl(CvRepository cvRepository, CvMapper cvMapper) {
        super(cvRepository, cvMapper, cvRepository::findById);
        this.cvRepository = cvRepository;
        this.cvMapper = cvMapper;
    }


    @Override
    public List<CvDto> GetCvsById(int customerId) {
        return cvRepository.findAllByCustomerIdAndStatus(customerId, CvStatus.ACTIVE)
                .stream()
                .map(cv -> {
                    CvDto cvDto = new CvDto();
                    cvDto.setId(cv.getId());
                    cvDto.setContent(cv.getContent());
                    cvDto.setSummary(cv.getSummary());
                    cvDto.setStatus(cv.getStatus());
                    cvDto.setCustomer(cv.getCustomer());
                    cvDto.setTemplate(cv.getTemplate());
                    cvDto.setContact(cv.getContact());
                    cvDto.setCertifications(
                            cv.getCertifications().stream()
                                    .filter(certification -> certification.getStatus() == CvStatus.ACTIVE)
                                    .map(certification -> {
                                        CertificationViewDto certificationViewDto = new CertificationViewDto();
                                        certificationViewDto.setId(certification.getId());
                                        certificationViewDto.setTitle(certification.getTitle());
                                        certificationViewDto.setCertificateSource(certification.getCertificateSource());
                                        certificationViewDto.setEndDate(certification.getEndDate());
                                        certificationViewDto.setCertificateRelevance(certification.getCertificateRelevance());
                                        certificationViewDto.setStatus(certification.getStatus());
                                        return certificationViewDto;
                                    })
                                    .collect(Collectors.toList())
                    );
                    cvDto.setEducations(
                            cv.getEducations().stream()
                                    .filter(education -> education.getStatus() == CvStatus.ACTIVE)
                                    .map(education -> {
                                        EducationViewDto educationViewDto = new EducationViewDto();
                                        educationViewDto.setId(education.getId());
                                        educationViewDto.setDegree(education.getDegree());
                                        educationViewDto.setNameCollege(education.getNameCollege());
                                        educationViewDto.setLocation(education.getLocation());
                                        educationViewDto.setEndDate(education.getEndDate());
                                        educationViewDto.setMinor(education.getMinor());
                                        educationViewDto.setGpa(education.getGpa());
                                        educationViewDto.setDescription(education.getDescription());
                                        educationViewDto.setStatus(education.getStatus());
                                        return educationViewDto;
                                    })
                                    .collect(Collectors.toList())
                    );
                    cvDto.setExperiences(
                            cv.getExperiences().stream()
                                    .filter(experience -> experience.getStatus() == CvStatus.ACTIVE)
                                    .map(experience -> {
                                        ExperienceViewDto experienceViewDto = new ExperienceViewDto();
                                        experienceViewDto.setId(experience.getId());
                                        experienceViewDto.setRoleCompany(experience.getRoleCompany());
                                        experienceViewDto.setName(experience.getName());
                                        experienceViewDto.setStartDate(experience.getStartDate());
                                        experienceViewDto.setEndDate(experience.getEndDate());
                                        experienceViewDto.setLocation(experience.getLocation());
                                        experienceViewDto.setDescription(experience.getDescription());
                                        experienceViewDto.setStatus(experience.getStatus());
                                        return experienceViewDto;
                                    })
                                    .collect(Collectors.toList())
                    );
                    cvDto.setInvolvements(
                            cv.getInvolvements().stream()
                                    .filter(involvement -> involvement.getStatus() == CvStatus.ACTIVE)
                                    .map(involvement -> {
                                        InvolvementViewDto involvementViewDto = new InvolvementViewDto();
                                        involvementViewDto.setId(involvement.getId());
                                        involvementViewDto.setOrganizationRole(involvement.getOrganizationRole());
                                        involvementViewDto.setOrganizationName(involvement.getOrganizationName());
                                        involvementViewDto.setStartDate(involvement.getStartDate());
                                        involvementViewDto.setEndDate(involvement.getEndDate());
                                        involvementViewDto.setCollegeLocation(involvement.getCollegeLocation());
                                        involvementViewDto.setDescription(involvement.getDescription());
                                        involvementViewDto.setStatus(involvement.getStatus());
                                        return involvementViewDto;
                                    })
                                    .collect(Collectors.toList())
                    );
                    cvDto.setProjects(
                            cv.getProjects().stream()
                                    .filter(project -> project.getStatus() == CvStatus.ACTIVE)
                                    .map(project -> {
                                        ProjectViewDto projectViewDto = new ProjectViewDto();
                                        projectViewDto.setId(project.getId());
                                        projectViewDto.setTitle(project.getTitle());
                                        projectViewDto.setOrganization(project.getOrganization());
                                        projectViewDto.setStartDate(project.getStartDate());
                                        projectViewDto.setEndDate(project.getEndDate());
                                        projectViewDto.setUrl(project.getUrl());
                                        projectViewDto.setDescription(project.getDescription());
                                        projectViewDto.setStatus(project.getStatus());
                                        return projectViewDto;
                                    })
                                    .collect(Collectors.toList())
                    );
                    cvDto.setSkills(
                            cv.getSkills().stream()
                                    .filter(skill -> skill.getStatus() == CvStatus.ACTIVE)
                                    .map(skill -> {
                                        SkillViewDto skillViewDto = new SkillViewDto();
                                        skillViewDto.setId(skill.getId());
                                        skillViewDto.setName(skill.getName());
                                        skillViewDto.setStatus(skill.getStatus());
                                        return skillViewDto;
                                    })
                                    .collect(Collectors.toList())
                    );
                    cvDto.setSourceWorks(
                            cv.getSourceWorks().stream()
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
                                    .collect(Collectors.toList())
                    );
                    return cvDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public CvDto GetCvsByCvId(int cvId) {
        Cv cv = cvRepository.findCvByIdAndStatus(cvId, CvStatus.ACTIVE);

        if (cv != null) {
            CvDto cvDto = new CvDto();
            cvDto.setId(cv.getId());
            cvDto.setContent(cv.getContent());
            cvDto.setSummary(cv.getSummary());
            cvDto.setStatus(cv.getStatus());
            cvDto.setCustomer(cv.getCustomer());
            cvDto.setTemplate(cv.getTemplate());
            cvDto.setContact(cv.getContact());
            cvDto.setCertifications(
                    cv.getCertifications().stream()
                            .filter(certification -> certification.getStatus() == CvStatus.ACTIVE)
                            .map(certification -> {
                                CertificationViewDto certificationViewDto = new CertificationViewDto();
                                certificationViewDto.setId(certification.getId());
                                certificationViewDto.setTitle(certification.getTitle());
                                certificationViewDto.setCertificateSource(certification.getCertificateSource());
                                certificationViewDto.setEndDate(certification.getEndDate());
                                certificationViewDto.setCertificateRelevance(certification.getCertificateRelevance());
                                certificationViewDto.setStatus(certification.getStatus());
                                return certificationViewDto;
                            })
                            .collect(Collectors.toList())
            );
            cvDto.setEducations(
                    cv.getEducations().stream()
                            .filter(education -> education.getStatus() == CvStatus.ACTIVE)
                            .map(education -> {
                                EducationViewDto educationViewDto = new EducationViewDto();
                                educationViewDto.setId(education.getId());
                                educationViewDto.setDegree(education.getDegree());
                                educationViewDto.setNameCollege(education.getNameCollege());
                                educationViewDto.setLocation(education.getLocation());
                                educationViewDto.setEndDate(education.getEndDate());
                                educationViewDto.setMinor(education.getMinor());
                                educationViewDto.setGpa(education.getGpa());
                                educationViewDto.setDescription(education.getDescription());
                                educationViewDto.setStatus(education.getStatus());
                                return educationViewDto;
                            })
                            .collect(Collectors.toList())
            );
            cvDto.setExperiences(
                    cv.getExperiences().stream()
                            .filter(experience -> experience.getStatus() == CvStatus.ACTIVE)
                            .map(experience -> {
                                ExperienceViewDto experienceViewDto = new ExperienceViewDto();
                                experienceViewDto.setId(experience.getId());
                                experienceViewDto.setRoleCompany(experience.getRoleCompany());
                                experienceViewDto.setName(experience.getName());
                                experienceViewDto.setStartDate(experience.getStartDate());
                                experienceViewDto.setEndDate(experience.getEndDate());
                                experienceViewDto.setLocation(experience.getLocation());
                                experienceViewDto.setDescription(experience.getDescription());
                                experienceViewDto.setStatus(experience.getStatus());
                                return experienceViewDto;
                            })
                            .collect(Collectors.toList())
            );
            cvDto.setInvolvements(
                    cv.getInvolvements().stream()
                            .filter(involvement -> involvement.getStatus() == CvStatus.ACTIVE)
                            .map(involvement -> {
                                InvolvementViewDto involvementViewDto = new InvolvementViewDto();
                                involvementViewDto.setId(involvement.getId());
                                involvementViewDto.setOrganizationRole(involvement.getOrganizationRole());
                                involvementViewDto.setOrganizationName(involvement.getOrganizationName());
                                involvementViewDto.setStartDate(involvement.getStartDate());
                                involvementViewDto.setEndDate(involvement.getEndDate());
                                involvementViewDto.setCollegeLocation(involvement.getCollegeLocation());
                                involvementViewDto.setDescription(involvement.getDescription());
                                involvementViewDto.setStatus(involvement.getStatus());
                                return involvementViewDto;
                            })
                            .collect(Collectors.toList())
            );
            cvDto.setProjects(
                    cv.getProjects().stream()
                            .filter(project -> project.getStatus() == CvStatus.ACTIVE)
                            .map(project -> {
                                ProjectViewDto projectViewDto = new ProjectViewDto();
                                projectViewDto.setId(project.getId());
                                projectViewDto.setTitle(project.getTitle());
                                projectViewDto.setOrganization(project.getOrganization());
                                projectViewDto.setStartDate(project.getStartDate());
                                projectViewDto.setEndDate(project.getEndDate());
                                projectViewDto.setUrl(project.getUrl());
                                projectViewDto.setDescription(project.getDescription());
                                projectViewDto.setStatus(project.getStatus());
                                return projectViewDto;
                            })
                            .collect(Collectors.toList())
            );
            cvDto.setSkills(
                    cv.getSkills().stream()
                            .filter(skill -> skill.getStatus() == CvStatus.ACTIVE)
                            .map(skill -> {
                                SkillViewDto skillViewDto = new SkillViewDto();
                                skillViewDto.setId(skill.getId());
                                skillViewDto.setName(skill.getName());
                                skillViewDto.setStatus(skill.getStatus());
                                return skillViewDto;
                            })
                            .collect(Collectors.toList())
            );
            cvDto.setSourceWorks(
                    cv.getSourceWorks().stream()
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
                            .collect(Collectors.toList())
            );

            return cvDto;
        } else {
            throw new IllegalArgumentException("CV not found with cvId: " + cvId);
        }
    }


    @Override
    public boolean updateCv(Integer id, CvAddNewDto dto) {
        Optional<Cv> existingCvOptional = cvRepository.findById(id);
        if (existingCvOptional.isPresent()) {
            Cv existingCv = existingCvOptional.get();
            if (!existingCv.getContent().equals(dto.getContent())) {
                if(dto.getContact() != null){
                    existingCv.setContent(dto.getContent());
                }else{
                    existingCv.setContent(existingCv.getContent());
                }
            } else {
                existingCv.setContent(existingCv.getContent());
            }
            if (!existingCv.getSummary().equals(dto.getSummary())) {
                if(dto.getSummary() != null){
                    existingCv.setSummary(dto.getSummary());
                }else{
                    existingCv.setSummary(existingCv.getSummary());
                }
            } else {
                existingCv.setSummary(existingCv.getSummary());
            }
            if(dto.getTemplate().getId()>0){
                existingCv.setTemplate(dto.getTemplate());
            }
            if(dto.getContact() != null && dto.getContact().getId() > 0){
                existingCv.setContact(dto.getContact());
            }

            existingCv.setStatus(CvStatus.ACTIVE);
            cvRepository.save(existingCv);
            return true;
        } else {
            throw new IllegalArgumentException("Certification ID not found");
        }
    }

    @Override
    public void deleteById(Integer id) {
        Optional<Cv> Optional = cvRepository.findById(id);
        if (Optional.isPresent()) {
            Cv cv = Optional.get();
            cv.setStatus(CvStatus.DELETED);
            cvRepository.save(cv);
        }
    }

    @Override
    public CvAddNewDto createCv(CvAddNewDto dto) {
        Cv cv = new Cv();
        cv.setContent(dto.getContent());
        cv.setSummary(dto.getSummary());
        cv.setStatus(dto.getStatus());
        cv.setCustomer(dto.getCustomer());

        Cv savedCv = cvRepository.save(cv);

        CvAddNewDto savedDto = new CvAddNewDto();
        savedDto.setId(savedCv.getId());
        savedDto.setContent(savedCv.getContent());
        savedDto.setSummary(savedCv.getSummary());
        savedDto.setStatus(savedCv.getStatus());
        savedDto.setCustomer(savedCv.getCustomer());

        return savedDto;
    }


}
