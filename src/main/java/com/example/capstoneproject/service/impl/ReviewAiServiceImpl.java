package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Dto.ReviewAiDto;
import com.example.capstoneproject.Dto.responses.ReviewAiViewDto;
import com.example.capstoneproject.entity.Cv;
import com.example.capstoneproject.entity.History;
import com.example.capstoneproject.entity.ReviewAi;
import com.example.capstoneproject.enums.BasicStatus;
import com.example.capstoneproject.exception.BadRequestException;
import com.example.capstoneproject.repository.CvRepository;
import com.example.capstoneproject.repository.HistoryRepository;
import com.example.capstoneproject.repository.ReviewAiRepository;
import com.example.capstoneproject.service.ReviewAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewAiServiceImpl implements ReviewAiService {
    @Autowired
    CvRepository cvRepository;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    ReviewAiRepository reviewAiRepository;

    @Override
    public void createReviewAi(Integer cvId, ReviewAiDto dto) {
        Optional<Cv> cvOptional = cvRepository.findByIdAndStatus(cvId, BasicStatus.ACTIVE);
        if(cvOptional.isPresent()){
            Cv cv = cvOptional.get();
            List<History> histories = historyRepository.findAllByCv_IdAndCv_StatusOrderByTimestampDesc(cv.getId(),BasicStatus.ACTIVE);
            if(histories!=null){
                History history = histories.get(0);
                ReviewAi reviewAi = new ReviewAi();
                Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                List<ReviewAi> reviewAis = reviewAiRepository.findAllByHistory_Id(history.getId());
                reviewAi.setVersion("Review #"+ reviewAis.size());
                reviewAi.setReview(dto.getReview());
                reviewAi.setTimestamp(currentTimestamp);
                reviewAi.setHistory(history);
                reviewAiRepository.save(reviewAi);
            }else{
                throw new BadRequestException("Please synchronize your CV before reviewing AI.");
            }
        }else{
            throw new BadRequestException("Cv id not found.");
        }
    }

    @Override
    public List<ReviewAiViewDto> getListReviewAi(Integer cvId) {
        List<ReviewAiViewDto> reviewAiViews = new ArrayList<>();
        Optional<Cv> cvOptional = cvRepository.findByIdAndStatus(cvId,BasicStatus.ACTIVE);
        if(cvOptional.isPresent()){
            Cv cv = cvOptional.get();
            List<ReviewAi> reviewAis = reviewAiRepository.findAllByHistory_Cv_Id(cv.getId());
            if(reviewAis!=null){
                for(ReviewAi reviewAi:reviewAis){
                    ReviewAiViewDto reviewAiView = new ReviewAiViewDto();
                    reviewAiView.setId(reviewAi.getId());
                    reviewAiView.setVersion(reviewAi.getVersion());
                    reviewAiView.setReview(reviewAi.getReview());
                    reviewAiView.setTimestamp(reviewAi.getTimestamp());
                    reviewAiViews.add(reviewAiView);
                }
            }
            return reviewAiViews;
        }else{
            throw new BadRequestException("Cv id not found.");
        }
    }
}
