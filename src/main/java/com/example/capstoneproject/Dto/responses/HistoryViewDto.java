package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.Dto.CvResumeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
public class HistoryViewDto {
    private Integer id;

    private String cvBody;

    private Timestamp timestamp;

    private CvResumeDto cv;
}
