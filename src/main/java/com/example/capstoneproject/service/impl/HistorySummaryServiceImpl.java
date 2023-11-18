package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.HistorySummaryDto;
import com.example.capstoneproject.Dto.responses.HistorySummaryViewDto;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.HistorySummary;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.repository.HistorySummaryRepository;
import com.example.capstoneproject.service.HistorySummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HistorySummaryServiceImpl implements HistorySummaryService {

    @Autowired
    HistorySummaryRepository historySummaryRepository;

    @Autowired
    CvRepository cvRepository;

    @Override
    public void createHistorySummary(Integer cvId, String summary) {
        Cv cv = cvRepository.findCvById(cvId, BasicStatus.ACTIVE);
        if(cv!=null){
            int amount = historySummaryRepository.countAllByCv_Id(cv.getId()) + 1;
            HistorySummary historySummary = new HistorySummary();
            historySummary.setVersion("");
            historySummary.setSummary("#" + amount + " SUMMARY");
            historySummary.setCv(cv);
            historySummaryRepository.save(historySummary);
        }else{
            throw new BadRequestException("Cv ID not found");
        }
    }

    @Override
    public List<HistorySummaryDto> getHistorySummaries(Integer cvId) {
        List<HistorySummary> historySummaries = historySummaryRepository.findAllByCv_Id(cvId);
        if(historySummaries!=null){
            List<HistorySummaryDto> historySummaryDtos = new ArrayList<>();
            HistorySummaryDto historySummaryDto = new HistorySummaryDto();
            for (HistorySummary historySummary1: historySummaries){
                historySummaryDto.setId(historySummary1.getId());
                historySummaryDto.setVersion(historySummary1.getVersion());
                historySummaryDtos.add(historySummaryDto);
            }
            return historySummaryDtos;
        }else{
            throw new BadRequestException("Currently, the system does not find any recorded summary. Please come back later.");
        }
    }

    @Override
    public HistorySummaryViewDto getHistorySummary(Integer summaryId) {
        HistorySummary historySummary = historySummaryRepository.getById(summaryId);
        if(historySummary==null){
            throw new BadRequestException("Summary History Id not found");
        }else{
            HistorySummaryViewDto historySummaryViewDto = new HistorySummaryViewDto();
            historySummaryViewDto.setSummary(historySummary.getSummary());
            return historySummaryViewDto;
        }
    }
}
