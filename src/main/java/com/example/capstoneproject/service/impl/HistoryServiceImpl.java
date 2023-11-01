package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.HistoryDto;
import com.example.capstoneproject.Dto.responses.HistoryViewDto;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.History;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.repository.HistoryRepository;
import com.example.capstoneproject.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    CvRepository cvRepository;

    @Override
    public HistoryViewDto create(Integer userId, Integer cvId, HistoryDto dto) {
        Optional<Cv> cvOptional = cvRepository.findByUser_IdAndId(userId, cvId);
        History history = new History();
        LocalDate current = LocalDate.now();
        if(cvOptional.isPresent()){
            Cv cv = cvOptional.get();
            history.setVersion(dto.getVersion());
            history.setTimestamp(current);
            history.setCvBody(cv.getCvBody());
            history.setCv(cv);
            historyRepository.save(history);
        }else {
            throw new RuntimeException("UserID not exist this CvID");
        }

        return null;
    }
}
