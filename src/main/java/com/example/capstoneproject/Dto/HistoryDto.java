package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.Cv;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.capstoneproject.Dto.responses.JobPostingApplyResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
public class HistoryDto {
    private Integer id;

    private CvBodyReviewDto cvBody;

    private String oldCvBody;

    private Timestamp timestamp;

    private Integer cvId;

    private JobPostingApplyResponse jobPosting;



    public CvBodyDto deserialize() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(this.oldCvBody, CvBodyDto.class);
    }
}
