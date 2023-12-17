package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.CandidateDto;
import com.example.capstoneproject.Dto.CvAddNewDto;
import com.example.capstoneproject.Dto.CvResumeDto;
import com.example.capstoneproject.Dto.UsersViewDto;
import com.example.capstoneproject.Dto.responses.CandidateOverViewDto;
import com.example.capstoneproject.Dto.responses.CandidateViewDto;
import com.example.capstoneproject.entity.Candidate;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.mapper.CvMapper;
import com.example.capstoneproject.mapper.UsersMapper;
import com.example.capstoneproject.repository.CandidateRepository;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.service.CandidateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    CvRepository cvRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CvMapper cvMapper;

    @Autowired
    UsersMapper usersMapper;

    @Override
    public boolean updateCandidate(Integer candidateId, CandidateDto dto) {
        Optional<Candidate> candidateOptional = candidateRepository.findByIdAndRole_RoleName(candidateId, RoleType.CANDIDATE);
        if(candidateOptional.isPresent()){
            Candidate candidate = candidateOptional.get();
            if(dto!=null){
                if (dto.getAvatar() != null && !dto.getAvatar().equals(candidate.getAvatar())) {
                    candidate.setAvatar(dto.getAvatar());
                }
                if (dto.getName() != null && !dto.getName().equals(candidate.getName())) {
                    candidate.setName(dto.getName());
                }
                if (dto.getJobTitle() != null && !dto.getJobTitle().equals(candidate.getJobTitle())) {
                    candidate.setJobTitle(dto.getJobTitle());
                }
                if (dto.getCompany() != null && !dto.getCompany().equals(candidate.getCompany())) {
                    candidate.setCompany(dto.getCompany());
                }
                if (dto.getAbout() != null && !dto.getAbout().equals(candidate.getAbout())) {
                    candidate.setAbout(dto.getAbout());
                }
                candidate.setPublish(dto.isPublish());
                candidateRepository.save(candidate);


                List<Cv> cvs = cvRepository.findAllByUsersIdAndStatus(candidateId, BasicStatus.ACTIVE);
                Integer[] cv = dto.getCv();

                for (Cv cvItem : cvs) {
                    if (Arrays.asList(cv).contains(cvItem.getId())) {
                        cvItem.setSearchable(true);
                    } else {
                        cvItem.setSearchable(false);
                    }
                    cvRepository.save(cvItem);
                }


                return true;
            }
        }else{
            throw new BadRequestException("Candidate ID not found");
        }
        return false;
    }

    @Override
    public CandidateViewDto getCandidateConfig(Integer candidateId) {
        CandidateViewDto candidateView = new CandidateViewDto();
        Optional<Candidate> candidateOptional = candidateRepository.findByIdAndRole_RoleName(candidateId, RoleType.CANDIDATE);
        if(candidateOptional.isPresent()){
            Candidate candidate =  candidateOptional.get();
            List<Cv> cvs = cvRepository.findAllByUser_IdAndStatusAndSearchableIsTrue(candidate.getId(),BasicStatus.ACTIVE);
            CvResumeDto[] cvArray = new CvResumeDto[cvs.size()];
            for (int i = 0; i < cvs.size(); i++) {
                Cv cv = cvs.get(i);
                CvResumeDto cvR = new CvResumeDto();
                cvR.setId(cv.getId());
                cvR.setResumeName(cv.getResumeName());
                cvArray[i] = cvR;
            }
            candidateView.setName(candidate.getName());
            candidateView.setAvatar(candidate.getAvatar());
            candidateView.setJobTitle(candidate.getJobTitle());
            candidateView.setCompany(candidate.getCompany());
            candidateView.setAbout(candidate.getAbout());
            candidateView.setPublish(candidate.isPublish());
// Assuming you have a setter for cv in CandidateViewDto
            candidateView.setCv(cvArray);
            return candidateView;
        }else{
            throw new BadRequestException("Candidate ID not found");
        }
    }

    public List<CandidateOverViewDto> getAllCandidatePublish(String search) {
        List<Candidate> candidates = candidateRepository.findAllByPublishTrueAndRole_RoleName(RoleType.CANDIDATE);
        List<CandidateOverViewDto> candidateOverViewDtos = new ArrayList<>();

        if(search==null){
            if (candidates != null) {
                candidateOverViewDtos = candidates.stream()
                        .map(candidate -> {
                            CandidateOverViewDto candidateOverViewDto = new CandidateOverViewDto();
                            candidateOverViewDto.setId(candidate.getId());
                            candidateOverViewDto.setName(candidate.getName());
                            candidateOverViewDto.setAvatar(candidate.getAvatar());
                            candidateOverViewDto.setJobTitle(candidate.getJobTitle());
                            candidateOverViewDto.setCompany(candidate.getCompany());
                            candidateOverViewDto.setAbout(candidate.getAbout());
                            return candidateOverViewDto;
                        })
                        .collect(Collectors.toList());
            } else {
                throw new BadRequestException("Currently, the system cannot find any published Candidates, please come back later.");
            }
        }else {
            if (candidates != null) {
                candidateOverViewDtos = candidates.stream()
                        .filter(candidate ->
                                (candidate.getName() != null && candidate.getName().contains(search)) ||
                                        (candidate.getJobTitle() != null && candidate.getJobTitle().contains(search)) ||
                                        (candidate.getCompany() != null && candidate.getCompany().contains(search)))
                        .map(candidate -> {
                            CandidateOverViewDto candidateOverViewDto = new CandidateOverViewDto();
                            candidateOverViewDto.setId(candidate.getId());
                            candidateOverViewDto.setName(candidate.getName());
                            candidateOverViewDto.setAvatar(candidate.getAvatar());
                            candidateOverViewDto.setJobTitle(candidate.getJobTitle());
                            candidateOverViewDto.setCompany(candidate.getCompany());
                            candidateOverViewDto.setAbout(candidate.getAbout());
                            return candidateOverViewDto;
                        })
                        .collect(Collectors.toList());
            } else {
                throw new BadRequestException("Currently, the system cannot find any published Candidates, please come back later.");
            }
        }

        return candidateOverViewDtos;
    }

    @Override
    public List<CvAddNewDto> getAllCvPublishCandidate(Integer candidateId) throws JsonProcessingException {
        List<Cv> cvs = cvRepository.findAllByUsersIdAndStatus(candidateId, BasicStatus.ACTIVE);
        List<CvAddNewDto> cvss = new ArrayList<>();
        if(!cvs.isEmpty()){
            for(Cv cv: cvs){
                if(cv.getSearchable()){
                    CvAddNewDto cvDto = cvMapper.cvAddNewDto(cv);
                    UsersViewDto usersViewDto = usersMapper.toView(cv.getUser());
                    modelMapper.map(usersViewDto, cvDto);
                    cvDto.getCertifications().clear();
                    cvDto.getExperiences().clear();
                    cvDto.getInvolvements().clear();
                    cvDto.getEducations().clear();
                    cvDto.getProjects().clear();
                    cvDto.getSkills().clear();
                    modelMapper.map(cv.deserialize(), cvDto);
                    cvss.add(cvDto);
                }
            }
        }
        return cvss;
    }


}
