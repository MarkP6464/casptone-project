package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.Dto.CvResumeDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class HistoryViewDto {
    private Integer id;

    private String version;

    private String cvBody;

    private LocalDate timestamp;

    private CvResumeDto cv;
}
