package com.example.capstoneproject.Dto;

import com.example.capstoneproject.entity.Cv;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class HistoryDto {
    private Integer id;

    private CvBodyReviewDto cvBody;

    private String oldCvBody;

    private Timestamp timestamp;

    private Integer cvId;

    public CvBodyDto deserialize() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(this.oldCvBody, CvBodyDto.class);
    }
}
