package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.Dto.UserCoverLetterDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class HistoryCoverLetterResponse {
    private int id;
    private String title;
    private String dear;
    private LocalDate date;
    private String description;
    private UserCoverLetterDto user;
    private CoverLetterViewDto coverLetter;
}
