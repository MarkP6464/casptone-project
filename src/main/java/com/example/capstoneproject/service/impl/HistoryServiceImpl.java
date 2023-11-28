package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.CvBodyDto;
import com.example.capstoneproject.Dto.CvBodyReviewDto;
import com.example.capstoneproject.Dto.HistoryDto;
import com.example.capstoneproject.Dto.responses.HistoryDateViewDto;
import com.example.capstoneproject.Dto.responses.HistoryViewDto;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.History;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.exception.ResourceNotFoundException;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.repository.HistoryRepository;
import com.example.capstoneproject.service.HistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    CvRepository cvRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    PrettyTime prettyTime;

    @Override
    public HistoryViewDto create(Integer userId, Integer cvId) throws JsonProcessingException {
        Optional<Cv> cvOptional = cvRepository.findByUser_IdAndId(userId, cvId);
        History history = new History();
        Instant currentInstant = Instant.now();
        Timestamp timestamp = Timestamp.from(currentInstant);
        if(cvOptional.isPresent()){
            Cv cv = cvOptional.get();
            CvBodyDto cvBodyDto = cv.deserialize();
            CvBodyReviewDto cvBodyReviewDto = new CvBodyReviewDto();
            cvBodyReviewDto.setCvStyle(cvBodyDto.getCvStyle());
            cvBodyReviewDto.setTemplateType(cvBodyDto.getTemplateType());
            cvBodyReviewDto.setSkills(cvBodyDto.getSkills());
            cvBodyReviewDto.setCertifications(cvBodyDto.getCertifications());
            cvBodyReviewDto.setExperiences(cvBodyDto.getExperiences());
            cvBodyReviewDto.setEducations(cvBodyDto.getEducations());
            cvBodyReviewDto.setInvolvements(cvBodyDto.getInvolvements());
            cvBodyReviewDto.setProjects(cvBodyDto.getProjects());
            cvBodyReviewDto.setSummary(cv.getSummary());
            cvBodyReviewDto.setName(cv.getUser().getName());
            cvBodyReviewDto.setAddress(cv.getUser().getAddress());
            cvBodyReviewDto.setPhone(cv.getUser().getPhone());
            cvBodyReviewDto.setPersonalWebsite(cv.getUser().getPersonalWebsite());
            cvBodyReviewDto.setEmail(cv.getUser().getEmail());
            cvBodyReviewDto.setLinkin(cv.getUser().getLinkin());
            // Sử dụng ObjectMapper để chuyển đổi CvBodyReviewDto thành chuỗi JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String cvBodyReviewJson = objectMapper.writeValueAsString(cvBodyReviewDto);
            history.setCvBody(cvBodyReviewJson);
            history.setTimestamp(timestamp);
            history.setCv(cv);
            return modelMapper.map(historyRepository.save(history), HistoryViewDto.class);
        }else {
            throw new BadRequestException("UserID not exist this CvID");
        }
    }

    @Override
    public List<HistoryDateViewDto> getListHistoryDate(Integer userId, Integer cvId) {
        Optional<Cv> cvOptional = cvRepository.findByUser_IdAndId(userId, cvId);
        if (cvOptional.isPresent()) {
            Cv cv = cvOptional.get();
            List<History> histories = historyRepository.findAllByCv_IdAndCv_StatusOrderByTimestampDesc(cv.getId(), BasicStatus.ACTIVE);
            return histories.stream()
                    .map(history -> {
                        HistoryDateViewDto dto = modelMapper.map(history, HistoryDateViewDto.class);
                        dto.setTimestamp(prettyTime.format(history.getTimestamp()));
                        return dto;
                    })
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    @Override
    public HistoryDto getHistory(Integer historyId) throws JsonProcessingException {
        HistoryDto historyViewDto = new HistoryDto();
        Optional<History> historyOptional = historyRepository.findById(historyId);
        if(historyOptional.isPresent()){
            History history = historyOptional.get();
            CvBodyReviewDto cvBodyReviewDto = history.deserialize();
            historyViewDto.setId(history.getId());
            historyViewDto.setTimestamp(history.getTimestamp());
            historyViewDto.setCvBody(cvBodyReviewDto);
            return  historyViewDto;
        }else {
            throw new ResourceNotFoundException("History ID not exist into Cv ID");
        }
    }


    @Override
    public HistoryDto getHistoryById(Integer historyId){

        Optional<History> historyOptional = historyRepository.findById(historyId);
        if (historyOptional.isPresent()){
            History history = historyOptional.get();
            HistoryDto historyDto = modelMapper.map(history, HistoryDto.class);
            historyDto.setCvId(history.getCv().getId());
            return historyDto;
        } else throw new ResourceNotFoundException("Not found history of the cover letter id!");
    }

}
