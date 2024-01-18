package com.example.capstoneproject.Dto.responses;

import com.example.capstoneproject.Dto.CvResumeDto;
import com.example.capstoneproject.enums.ApplicationLogStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;

@NoArgsConstructor
@Getter
@Setter
public class ApplicationLogFullResponse {
    String candidateName;
    Timestamp applyDate;
    String note;
    String email;
    ApplicationLogStatus status;
    JobPostingNameViewDto jobPosting;

    CvResumeDto cvs;
    CoverLetterViewDto coverLetters;
}
