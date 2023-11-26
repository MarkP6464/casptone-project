package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.CandidateDto;
import com.example.capstoneproject.Dto.responses.CandidateOverViewDto;
import com.example.capstoneproject.entity.Candidate;
import com.example.capstoneproject.enums.RoleType;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.repository.CandidateRepository;
import com.example.capstoneproject.service.CandidateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public boolean updateCandidate(Integer candidateId, CandidateDto dto) {
        Optional<Candidate> candidateOptional = candidateRepository.findByIdAndRole_RoleName(candidateId, RoleType.CANDIDATE);
        CandidateDto candidateDto = new CandidateDto();
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
                return true;
            }
        }else{
            throw new BadRequestException("Candidate ID not found");
        }
        return false;
    }

    @Override
    public CandidateDto getCandidateConfig(Integer candidateId) {
        Optional<Candidate> candidateOptional = candidateRepository.findByIdAndRole_RoleName(candidateId, RoleType.CANDIDATE);
        if(candidateOptional.isPresent()){
            Candidate candidate =  candidateOptional.get();
            return modelMapper.map(candidate, CandidateDto.class);
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
                                candidate.getName().contains(search) ||
                                        candidate.getJobTitle().contains(search) ||
                                        candidate.getCompany().contains(search))
                        .map(candidate -> {
                            CandidateOverViewDto candidateOverViewDto = new CandidateOverViewDto();
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


}